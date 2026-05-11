package com.toerso.healthconnect.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuditLogRequest {
    private String actorUsername;
    private String action;
    private String targetType;
    private Long targetId;
    private String ipAddress;
    private String status;
    private String failureReason;
    private Long executionTimeMs;
}