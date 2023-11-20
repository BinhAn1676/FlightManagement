package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.FileDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileDataEntity,Long> {
    Optional<FileDataEntity> findByName(String fileName);
}
