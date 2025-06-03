package com.example.health_management.domain.services.exporters;

import com.example.health_management.common.utils.exports.ExcelUtils;
import com.example.health_management.domain.entities.DoctorSchedule;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ExcelScheduleExporter {
    public static Workbook export(List<DoctorSchedule> schedules) {
        try (InputStream templateStream = new ClassPathResource("templates/xlsx/template_doctor_schedule.xlsx").getInputStream()) {
            Workbook workbook = new XSSFWorkbook(templateStream);
            Sheet sheet = workbook.getSheetAt(0);

            String date = LocalDateTime.now().toLocalDate().toString();
            sheet.getRow(1).getCell(4).setCellValue(
                    sheet.getRow(1).getCell(4).getStringCellValue().replace("${date}", date)
            );

            int rowIndex = 4;
            int index = 1;

            CellStyle center = ExcelUtils.createCenterStyle(workbook);
            CellStyle left = ExcelUtils.createLeftStyle(workbook);

            for (DoctorSchedule s : schedules) {
                if( s == null) continue;
                Row row = sheet.createRow(rowIndex++);
                ExcelUtils.writeRow(row, center, left, Arrays.asList(
                        String.valueOf(index++)
                        //data fill
                ));
            }

            return workbook;
        } catch (IOException e) {
            throw new RuntimeException("Error exporting schedules to Excel", e);
        }
    }
}
