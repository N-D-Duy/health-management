package com.example.health_management.application.apiresponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {

    // Getter và Setter
    private int code;
    private String message;
    private Object data; // Dữ liệu trả về, nếu có

    // default constructor
    public ApiResponse() {
        this.code = HttpServletResponse.SC_OK; // 200
        this.message = "Success";
    }

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
