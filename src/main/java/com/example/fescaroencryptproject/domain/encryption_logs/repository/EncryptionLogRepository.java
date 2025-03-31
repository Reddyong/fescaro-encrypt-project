package com.example.fescaroencryptproject.domain.encryption_logs.repository;

import com.example.fescaroencryptproject.domain.encryption_logs.entity.EncryptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptionLogRepository extends JpaRepository<EncryptionLog, Long> {
}
