package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find all actions performed by a specific user
    List<AuditLog> findByActorUsernameOrderByTimestampDesc(String username);

    // Find all actions on a specific entity type and record
    // e.g. "show me everything that happened to Patient #42"
    List<AuditLog> findByTargetTypeAndTargetIdOrderByTimestampDesc(
            String targetType, Long targetId
    );

    // Find all actions of a specific type within a time range
    // e.g. "show all DELETE_PATIENT actions this week"
    List<AuditLog> findByActionAndTimestampBetween(
            String action,
            LocalDateTime from,
            LocalDateTime to
    );

    // Find all failed actions — useful for security monitoring
    List<AuditLog> findByStatusOrderByTimestampDesc(String status);
}
