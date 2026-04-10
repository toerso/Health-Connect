package com.toerso.healthconnect.service;

public interface AuditLogService {
    void log(String username, String action, String type, Long id);
}
