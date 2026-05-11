package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.dto.request.AuditLogRequest;
import com.toerso.healthconnect.entity.AuditLog;
import com.toerso.healthconnect.repository.AuditLogRepository;
import com.toerso.healthconnect.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(AuditLogRequest request) {
        AuditLog log = AuditLog.builder()
                .actorUsername(request.getActorUsername())
                .action(request.getAction())
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .timestamp(LocalDateTime.now())
                .ipAddress(request.getIpAddress())
                .status(request.getStatus())
                .failureReason(request.getFailureReason())
                .executionTimeMs(request.getExecutionTimeMs())
                .build();

        auditLogRepository.save(log);
    }
}
