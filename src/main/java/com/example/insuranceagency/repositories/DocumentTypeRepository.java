package com.example.insuranceagency.repositories;

import com.example.insuranceagency.entities.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    @Override
    List<DocumentType> findAll();

    Optional<DocumentType> findByTitle(String title);

}
