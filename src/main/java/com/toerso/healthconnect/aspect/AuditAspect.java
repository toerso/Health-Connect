package com.toerso.healthconnect.aspect;

import com.toerso.healthconnect.annotation.audit.Audited;
import com.toerso.healthconnect.dto.request.AuditLogRequest;
import com.toerso.healthconnect.service.AuditLogService;
import com.toerso.healthconnect.utility.IpExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final IpExtractor ipExtractor;

    // HttpServletRequest gives us the current HTTP request
    // Spring provides this via RequestContextHolder
    private final HttpServletRequest request;

    @Around("@annotation(audited)")
    public Object audit(ProceedingJoinPoint pjp, Audited audited) throws Throwable {

        long startTime = System.currentTimeMillis();

        // Extract WHO is doing this — from Spring Security context
        String actor = extractActor();

        // Extract WHERE the request came from
        String ip = ipExtractor.extractIp(request);

        // Extract WHICH specific record is being affected
        // Uses the targetIdArgIndex from the annotation
        Long targetId = extractTargetId(pjp, audited.targetIdArgIndex());

        String status = "";
        String failureReason = null;
        Object result;

        try {
            result = pjp.proceed();
            status = "SUCCESS";
        } catch (Throwable ex) {
            status = "FAILED";
            failureReason = ex.getClass().getSimpleName() + ": " + ex.getMessage();
            throw ex;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

            AuditLogRequest logRequest = AuditLogRequest.builder()
                    .actorUsername(actor)
                    .action(audited.action())
                    .targetType(audited.targetType())
                    .targetId(targetId)
                    .ipAddress(ip)
                    .status(status)
                    .failureReason(failureReason)
                    .executionTimeMs(executionTime)
                    .build();

            try {
                auditLogService.log(logRequest);
            } catch (Exception logEx) {
                // If audit logging itself fails, log the error but
                // do NOT let it bubble up and break the actual operation
                log.error("Failed to write audit log for action [{}]: {}",
                        audited.action(), logEx.getMessage());
            }
        }

        return result;
    }

    private String extractActor() {
        try {
            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (auth != null && auth.isAuthenticated()) {
                return auth.getName();
            }
        } catch (Exception e) {
            log.warn("Could not extract actor from security context");
        }
        return "anonymous";
    }

    private Long extractTargetId(ProceedingJoinPoint pjp, int argIndex) {
        // -1 means the annotation didn't specify an ID argument
        if (argIndex < 0) {
            return null;
        }

        Object[] args = pjp.getArgs();

        // Guard against wrong index being specified in annotation
        if (argIndex >= args.length) {
            log.warn("targetIdArgIndex {} is out of bounds for method {}",
                    argIndex, pjp.getSignature().getName());
            return null;
        }

        Object arg = args[argIndex];

        if (arg instanceof Long) {
            return (Long) arg;
        }

        if (arg instanceof Integer) {
            return ((Integer) arg).longValue();
        }

        // If the argument is not a numeric ID, we cannot extract it
        log.warn("Argument at index {} is not a numeric ID type: {}",
                argIndex, arg.getClass().getSimpleName());
        return null;
    }
}
