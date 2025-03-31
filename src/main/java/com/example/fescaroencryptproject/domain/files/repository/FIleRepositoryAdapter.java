package com.example.fescaroencryptproject.domain.files.repository;

import com.example.fescaroencryptproject.domain.files.entity.File;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class FIleRepositoryAdapter implements FileRepositoryPort {
    private final FileRepository fileRepository;

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File findById(Long id) {
        return fileRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Page<File> findAll(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

}
