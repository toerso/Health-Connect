package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
