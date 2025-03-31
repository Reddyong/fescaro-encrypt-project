package com.example.fescaroencryptproject.domain.files.repository;

import com.example.fescaroencryptproject.domain.files.entity.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FIleRepositoryAdapter implements FileRepositoryPort {
    private final FileRepository fileRepository;

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }
}
