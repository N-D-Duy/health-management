package com.example.health_management.domain.services.exporters;

import com.example.health_management.common.utils.exports.ExcelUtils;
import com.example.health_management.domain.entities.DoctorSchedule;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ExcelScheduleExporter {
    public Workbook export(List<DoctorSchedule> schedules, String queryDate) {
        try (InputStream templateStream = new ClassPathResource("templates/xlsx/template_doctor_schedule.xlsx").getInputStream()) {
            Workbook workbook = new XSSFWorkbook(templateStream);
            Sheet sheet = workbook.getSheetAt(0);

            sheet.getRow(0).getCell(0).setCellValue(
                    sheet.getRow(0).getCell(0).getStringCellValue().replace("${date}", queryDate)
            );

            String date = LocalDateTime.now().toLocalDate().toString();
            sheet.getRow(1).getCell(5).setCellValue(
                    sheet.getRow(1).getCell(5).getStringCellValue().replace("${exportDate}", date)
            );

            String doctorName = schedules.isEmpty() ? "Unknown Doctor" : schedules.get(0).getDoctor().getUser().getFirstName() + " " + schedules.get(0).getDoctor().getUser().getLastName();
            sheet.getRow(2).getCell(5).setCellValue(
                    sheet.getRow(2).getCell(5).getStringCellValue().replace("${doctorName}", doctorName)
            );
            int rowIndex = 5;
            int index = 1;

            CellStyle center = ExcelUtils.createCenterStyle(workbook);
            CellStyle left = ExcelUtils.createLeftStyle(workbook);

            for (DoctorSchedule s : schedules) {
                if( s == null) continue;
                Row row = sheet.createRow(rowIndex++);
                ExcelUtils.writeRow(row, center, left, Arrays.asList(
                        String.valueOf(index++),
                        s.getStartTime().toLocalDate().toString(),
                        s.getStartTime().toLocalTime().toString(),
                        s.getPatientName(),
                        s.getExaminationType() != null ? s.getExaminationType() : "N/A",
                        s.getAppointmentStatus() != null ? s.getAppointmentStatus().toString() : "N/A",
                        s.getNote() != null ? s.getNote() : "N/A"
                ));
            }

            return workbook;
        } catch (IOException e) {
            throw new RuntimeException("Error exporting schedules to Excel", e);
        }
    }
}
