package com.example.fescaroencryptproject.domain.files.repository;

import com.example.fescaroencryptproject.domain.files.entity.File;

public interface FileRepositoryPort {
    File save(File file);
}
