package com.techpalle.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "audit_logs",
        indexes = {
                @Index(name = "idx_entity_type", columnList = "entity_type"),
                @Index(name = "idx_operation", columnList = "operation"),
                @Index(name = "idx_user", columnList = "audit_user"),
                @Index(name = "idx_created_at", columnList = "created_at")
        })

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate

public class AuditLog extends BaseEntity{
	
    @Column(name = "entity_type", length = 100, nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private AuditOperation operation;

    @Lob
    @Column(name = "old_value")
    private String oldValue;

    @Lob
    @Column(name = "new_value")
    private String newValue;

    @Column(name = "audit_user", length = 100)
    private String auditUser;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuditStatus status;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    // ================= ENUMS =================

    public enum AuditOperation {
        CREATE,
        UPDATE,
        DELETE,
        DEPOSIT,
        WITHDRAW,
        TRANSFER
    }

    public enum AuditStatus {
        SUCCESS,
        FAILURE
    }
}
