package com.example.health_management.common.utils.handle_privilege.doctor_access;

import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.guards.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DoctorAccessAspect {
    private final JwtProvider jwtProvider;

    @Before("@annotation(doctorAccess)")
    public void checkDoctorAccess(JoinPoint joinPoint, DoctorAccess doctorAccess) {
        String paramName = doctorAccess.paramName();
        Long doctorId = extractDoctorId(joinPoint, paramName);
        if(doctorId == null) {
            throw new AccessDeniedException("Invalid doctor ID");
        }

        MyUserDetails currentUser = jwtProvider.extractUserDetailsFromToken();

        // Case 1: If ADMIN, allow everything
        if(currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return;
        }

        // Case 2: If MANAGER, check doctor:action permission
        if(currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            String requiredPermission = "doctor:" + doctorAccess.action().name().toLowerCase();
            if(currentUser.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(requiredPermission))) {
                return;
            }
        }

        // Case 3: If DOCTOR, check if it's self-modification
        if(currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
            if(currentUser.getId().equals(doctorId)) {
                return;
            }
        }

        throw new AccessDeniedException("You don't have permission to perform this action");
    }

    private Long extractDoctorId(JoinPoint joinPoint, String paramName) {
        Object[] args = joinPoint.getArgs();
        if(args == null) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof Long && paramName.equals("doctorId")) {
                return (Long) arg;
            }
        }
        return null;
    }
}