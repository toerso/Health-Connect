package com.toerso.healthconnect.service;

import com.toerso.healthconnect.dto.request.AuditLogRequest;

public interface AuditLogService {
    void log(AuditLogRequest request);
}
