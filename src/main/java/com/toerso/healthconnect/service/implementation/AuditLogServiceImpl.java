package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.entity.AuditLog;
import com.toerso.healthconnect.repository.AuditLogRepository;
import com.toerso.healthconnect.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    @Async
    public void log(String username, String action, String type, Long id) {
        AuditLog auditLog = AuditLog.builder()
                .actorUsername(username)
                .action(action)
                .targetType(type)
                .targetId(id)
                .build();
        auditLogRepository.save(auditLog);
    }
}
