package com.example.health_management.application.apiresponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code;
    private String message; // Thông điệp trả về, mặc định là "Success"
    private T data; // Dữ liệu trả về, nếu có

//     default constructor
    public ApiResponse() {
        this.code = HttpServletResponse.SC_OK; // 200
        this.message = "Success";
        this.data = null;
    }
}
