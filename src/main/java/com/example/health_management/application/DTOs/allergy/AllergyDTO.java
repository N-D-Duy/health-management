package com.example.health_management.application.DTOs.allergy;


import com.example.health_management.common.shared.enums.AllergyType;
import lombok.Data;

@Data
public class AllergyDTO {
    private Long id;
    private AllergyType allergyType;
    private String severity;
    private String note;
}
