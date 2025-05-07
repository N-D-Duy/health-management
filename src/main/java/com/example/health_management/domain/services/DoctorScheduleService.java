package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.mapper.DoctorScheduleMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.DoctorSchedule;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    public void createDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        Doctor doctor = doctorRepository.findByIdActive(doctorScheduleDTO.getDoctorId());
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setStartTime(doctorScheduleDTO.getStartTime());
        doctorSchedule.setEndTime(doctorScheduleDTO.getEndTime());
        doctorSchedule.setCurrentPatientCount(doctorScheduleDTO.getCurrentPatientCount());

        doctorScheduleRepository.save(doctorSchedule);
    }

    public List<DoctorScheduleDTO> getDoctorSchedules(Long doctorId) {
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findAllByDoctorId(doctorId);
        return doctorSchedules.stream().map(doctorScheduleMapper::toDTO).toList();
    }

    public void updateDoctorSchedule(DoctorSchedule doctorSchedule, Boolean increase) {
        if(increase){
            doctorScheduleRepository.updatePatientsCount(doctorSchedule.getId(), 1);
        } else {
            if(doctorSchedule.getCurrentPatientCount() == 1){
                //remove the schedule
                doctorScheduleRepository.delete(doctorSchedule);
            }
            doctorScheduleRepository.updatePatientsCount(doctorSchedule.getId(), -1);
        }
    }

    public void updateOrCreateDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO, Boolean increase) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByTimes(doctorScheduleDTO.getDoctorId(), doctorScheduleDTO.getStartTime(), doctorScheduleDTO.getEndTime());

        if(doctorSchedule == null){
            createDoctorSchedule(doctorScheduleDTO);
        } else {
            if(increase){
                if(!doctorSchedule.isAvailable()){
                    throw new ConflictException("Doctor is already busy");
                }
                updateDoctorSchedule(doctorSchedule, true);
            } else {
                updateDoctorSchedule(doctorSchedule, false);
            }
        }
    }
}
