package com.example.fescaroencryptproject.domain.encryption_keys.repository;

import com.example.fescaroencryptproject.domain.encryption_keys.entity.EncryptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptionKeyRepository extends JpaRepository<EncryptionKey, Long> {
}
