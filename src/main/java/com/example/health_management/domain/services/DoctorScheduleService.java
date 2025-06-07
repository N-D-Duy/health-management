package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.mapper.DoctorScheduleMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.common.utils.exports.ExcelUtils;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.DoctorSchedule;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.DoctorScheduleRepository;
import com.example.health_management.domain.services.exporters.ExcelScheduleExporter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final ExcelScheduleExporter excelScheduleExporter;

    public void createDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        if (doctorScheduleRepository.existsByPatientNameAndStartTime(doctorScheduleDTO.getPatientName(), doctorScheduleDTO.getStartTime())) {
            throw new ConflictException("Patient already has a schedule at this time");
        }
        if(isDoctorBusy(doctorScheduleDTO.getDoctorId(), doctorScheduleDTO.getStartTime())) {
            throw new ConflictException("Doctor is busy at this time");
        }
        Doctor doctor = doctorRepository.findByIdActive(doctorScheduleDTO.getDoctorId());
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setStartTime(doctorScheduleDTO.getStartTime());
        doctorSchedule.setPatientName(doctorScheduleDTO.getPatientName());
        doctorSchedule.setExaminationType(doctorScheduleDTO.getExaminationType());
        doctorSchedule.setAppointmentStatus(doctorScheduleDTO.getAppointmentStatus());
        doctorSchedule.setNote(doctorScheduleDTO.getNote());

        doctorScheduleRepository.save(doctorSchedule);
    }

    public List<DoctorScheduleDTO> getDoctorSchedules(Long doctorId) {
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findAllByDoctorId(doctorId);
        return doctorSchedules.stream().map(doctorScheduleMapper::toDTO).toList();
    }

    public void updateDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(doctorScheduleDTO.getId())
                .orElse(null);

        if (doctorSchedule == null) {
            throw new ConflictException("Doctor schedule not found");
        }
        doctorScheduleMapper.update(doctorSchedule, doctorScheduleDTO);
        doctorScheduleRepository.save(doctorSchedule);
    }

    public void updateDoctorScheduleStatus(Long doctorScheduleId, String status) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(doctorScheduleId)
                .orElseThrow(() -> new ConflictException("Doctor schedule not found"));
        doctorSchedule.setAppointmentStatus(status);
        doctorScheduleRepository.save(doctorSchedule);
    }

    public void deleteDoctorSchedule(Long doctorScheduleId) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(doctorScheduleId)
                .orElseThrow(() -> new ConflictException("Doctor schedule not found"));
        doctorScheduleRepository.delete(doctorSchedule);
    }

    public boolean isDoctorBusy(Long doctorId, LocalDateTime startTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        int bookedCount = doctorScheduleRepository.countDoctorSchedulesAtTime(doctorId, startTime);
        int maxPatients = doctor.getSpecialization().getMaxPatients();

        return bookedCount >= maxPatients;
    }

    public ByteArrayResource exportDoctorSchedules(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorIdAndDateRange(doctorId, startDate, endDate);
        if (schedules == null || schedules.isEmpty()) {
            throw new ConflictException("No schedules found for the given date range");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        String range = ExcelUtils.extractDateFromRequest(startDate, endDate);
        Workbook workbook = excelScheduleExporter.export(schedules, range);
        String doctorName = schedules.get(0).getDoctor().getUser().getFirstName() + " " + schedules.get(0).getDoctor().getUser().getLastName();
        String fileName = "Doctor_Schedule_" + doctorName + ".xlsx";
        return ExcelUtils.toByteArrayResource(workbook, fileName);
    }

}
