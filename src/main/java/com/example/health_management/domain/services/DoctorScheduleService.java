package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.doctor.DoctorAvailableResponse;
import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.mapper.DoctorScheduleMapper;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.common.utils.exports.ExcelUtils;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.DoctorSchedule;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.DoctorScheduleRepository;
import com.example.health_management.domain.repositories.UserRepository;
import com.example.health_management.domain.services.exporters.ExcelScheduleExporter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final ExcelScheduleExporter excelScheduleExporter;
    private final UserRepository userRepository;

    public void createDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        if (doctorScheduleRepository.patientBusyAtTime(doctorScheduleDTO.getPatientName(), doctorScheduleDTO.getStartTime())) {
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
        doctorSchedule.setAppointmentRecord(doctorScheduleDTO.getAppointmentRecord());

        doctorScheduleRepository.save(doctorSchedule);
    }

    public List<DoctorScheduleDTO> getDoctorSchedules(Long doctorId) {
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findAllByDoctorId(doctorId);
        return doctorSchedules.stream().map(doctorScheduleMapper::toDTO).toList();
    }

    public List<DoctorAvailableResponse> getBusyTimes(Long doctorId) {
        List<DoctorSchedule> schedules = doctorScheduleRepository.findAllByDoctorId(doctorId);
        return schedules.stream()
                .map(schedule -> DoctorAvailableResponse.builder()
                        .time(schedule.getStartTime())
                        .isAvailable(!isDoctorBusy(doctorId, schedule.getStartTime()))
                        .build())
                .distinct()
                .filter(schedule -> !schedule.isAvailable() && schedule.getTime().isAfter(LocalDateTime.now()))
                .toList();
    }

    //combine the busy times for the doctor (schedules with no 'CANCELLED' status) and the patient (the schedules that the patient has with no 'CANCELLED' status)
    public List<DoctorAvailableResponse> getBusyTimesForPatient(Long doctorId, Long patientId) {
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findAllByDoctorId(doctorId)
                .stream()
                .filter(ds -> ds.getAppointmentStatus() == AppointmentStatus.SCHEDULED)
                .toList();

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ConflictException("Patient not found"));
        String patientName = patient.getFirstName() + " " + patient.getLastName();

        List<DoctorSchedule> patientSchedules = doctorScheduleRepository.findByPatientName(patientName).stream()
                .filter(ds -> ds.getPatientName().equals(patientName))
                .toList();

        Set<LocalDateTime> busyTimes = new HashSet<>();
        doctorSchedules.forEach(ds -> busyTimes.add(ds.getStartTime()));
        patientSchedules.forEach(ds -> busyTimes.add(ds.getStartTime()));

        return busyTimes.stream()
                .map(time -> DoctorAvailableResponse.builder()
                        .time(time)
                        .isAvailable(false)
                        .build())
                .sorted(Comparator.comparing(DoctorAvailableResponse::getTime))
                .toList();
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

    public void updateDoctorScheduleStatusByAppointmentId(Long appointmentId, AppointmentStatus status) {
        doctorScheduleRepository.updateAppointmentStatusByAppointmentId(appointmentId, status);
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
