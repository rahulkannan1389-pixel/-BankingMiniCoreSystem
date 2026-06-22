package com.techpalle.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpalle.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Serializable> {

	   List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType,Long entityId);

	   List<AuditLog> findByOperationOrderByCreatedAtDesc(AuditLog.AuditOperation operation);

	   List<AuditLog> findByStatusOrderByCreatedAtDesc( AuditLog.AuditStatus status);


}
