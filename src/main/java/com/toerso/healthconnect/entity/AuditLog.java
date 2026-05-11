package com.toerso.healthconnect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // WHO did it
    private String actorUsername;

    // WHAT they did — comes from @Audited(action = "...")
    private String action;

    // WHICH entity was affected — e.g. "Patient", "User"
    private String targetType;

    // WHICH specific record was affected — e.g. patient ID 42
    private Long targetId;

    // WHEN it happened
    private LocalDateTime timestamp;

    // WHERE the request came from
    private String ipAddress;

    // DID IT SUCCEED or FAILED
    private String status;

    // WHAT WENT WRONG if it failed
    @Column(length = 1000)
    private String failureReason;

    // HOW LONG it took in milliseconds
    private Long executionTimeMs;
}