package com.example.health_management.common.utils.handle_privilege.self_privilege;

import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.guards.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserMatchAspect {
    private final JwtProvider jwtProvider;

    /*compare id to check privilege (btw token and request)*/
    @Before("@annotation(checkUserMatch)")
    public void checkUserMatch(JoinPoint joinPoint, CheckUserMatch checkUserMatch) {
        String paramName = checkUserMatch.paramName();
        Long userId = extractUserId(joinPoint, paramName);
        if(userId == null) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }

        // Check if admin, then allow
        MyUserDetails currentUser = jwtProvider.extractUserDetailsFromToken();
        if(currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return;
        }

        // Check match
        if (!currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }
    }

    private Long extractUserId(JoinPoint joinPoint, String paramName) {
        Object[] args = joinPoint.getArgs();
        if(args == null) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof Long && paramName.equals("userId")) {
                return (Long) arg;
            }
        }
        return null;
    }
}

