package com.example.health_management.application.apiresponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
@Builder
public class ApiResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    
    public ApiResponse() {
        this.code = HttpServletResponse.SC_OK; // 200
        this.message = "Success";
        this.data = null;
    }
}
