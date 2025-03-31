package com.example.fescaroencryptproject.domain.encryption_keys.repository;

import com.example.fescaroencryptproject.domain.encryption_keys.entity.EncryptionKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EncryptionKeyRepositoryAdapter implements EncryptionKeyRepositoryPort {
    private final EncryptionKeyRepository encryptionKeyRepository;

    @Override
    public EncryptionKey save(EncryptionKey encryptionKey) {
        return encryptionKeyRepository.save(encryptionKey);
    }

}
