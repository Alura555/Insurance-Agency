package com.example.insuranceagency.repository;

import com.example.insuranceagency.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    @Override
    List<DocumentType> findAll();

    Optional<DocumentType> findByTitle(String title);

}
