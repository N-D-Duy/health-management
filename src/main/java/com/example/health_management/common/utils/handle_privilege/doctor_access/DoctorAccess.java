package com.example.health_management.common.utils.handle_privilege.doctor_access;

import com.example.health_management.common.shared.enums.DoctorAction;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated()")
public @interface DoctorAccess {
    String paramName() default "doctorId";
    DoctorAction action() default DoctorAction.READ;
}
