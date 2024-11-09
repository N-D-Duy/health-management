package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.analysis.DataAnalysis;
import com.example.health_management.domain.repositories.analytics.AppointmentStatusProjection;
import com.example.health_management.domain.repositories.analytics.DataAnalysisRepository;
import com.example.health_management.domain.repositories.analytics.UserStatisticsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DataAnalysisService {
    private final DataAnalysisRepository dataAnalysisRepository;

    public DataAnalysis getDataAnalysis(LocalDate startDate, LocalDate endDate) {
        DataAnalysis analysis = new DataAnalysis();

        // Thống kê người dùng
        UserStatisticsProjection userStats = dataAnalysisRepository.getUserStatistics(startDate, endDate);
        analysis.setTotalUsers(userStats.getTotalUser());
        analysis.setTotalPatients(userStats.getTotalPatient());
        analysis.setTotalDoctors(userStats.getTotalDoctor());

        // Thống kê giới tính
        List<Map<String, Long>> genderStats = dataAnalysisRepository.getUserCountByGender(startDate, endDate)
                .stream()
                .map(genderStat -> {
                    Map<String, Long> genderMap = new HashMap<>();
                    genderMap.put(genderStat.getGender(), genderStat.getCount());
                    return genderMap;
                })
                .collect(Collectors.toList());
        analysis.setGender(genderStats);

        // Thống kê cuộc hẹn
        Map<String, Long> appointmentStatusMap = dataAnalysisRepository.getAppointmentCountByStatus(startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        AppointmentStatusProjection::getStatus,
                        AppointmentStatusProjection::getCount
                ));

        analysis.setAppointmentsScheduled(
                appointmentStatusMap.getOrDefault("SCHEDULED", 0L)
        );
        analysis.setAppointmentsCompleted(
                appointmentStatusMap.getOrDefault("COMPLETED", 0L)
        );
        analysis.setAppointmentsCancelled(
                appointmentStatusMap.getOrDefault("CANCELLED", 0L)
        );



        // Thống kê bài viết
        analysis.setArticlesCreated(
                dataAnalysisRepository.getArticleCount(startDate, endDate)
        );

        return analysis;
    }
}
