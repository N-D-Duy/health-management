package com.example.health_management.common.utils.exports;

import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ExcelUtils {

    public static CellStyle createCenterStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        commonBorder(style);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    public static CellStyle createLeftStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        commonBorder(style);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private static void commonBorder(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    public static void writeRow(Row row, CellStyle center, CellStyle left, List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data.get(i));
            cell.setCellStyle(i == 0 ? center : left);
        }
    }

    public static ByteArrayResource toByteArrayResource(Workbook wb, String fileName) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            wb.write(os);
            return new ByteArrayResource(os.toByteArray()) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file", e);
        }
    }

    public static String extractDateFromRequest(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate == null || startDate.getMonth().equals(endDate.getMonth()) && startDate.getYear() == endDate.getYear()) {
            return "Tháng " + startDate.getMonthValue() + " Năm " + startDate.getYear();
        } else {
            return "Lịch sử khám từ " + startDate.getMonthValue() + "/" + startDate.getYear() +
                    " đến " + endDate.getMonthValue() + "/" + endDate.getYear();
        }
    }

}

