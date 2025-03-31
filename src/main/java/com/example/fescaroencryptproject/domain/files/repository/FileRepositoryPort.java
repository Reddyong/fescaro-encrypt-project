package com.example.fescaroencryptproject.domain.files.repository;

import com.example.fescaroencryptproject.domain.files.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileRepositoryPort {
    File save(File file);

    File findById(Long id);

    Page<File> findAll(Pageable pageable);
}
