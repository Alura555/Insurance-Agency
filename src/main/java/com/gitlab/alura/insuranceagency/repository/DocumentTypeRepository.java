package com.gitlab.alura.insuranceagency.repository;

import com.gitlab.alura.insuranceagency.entity.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    @Override
    List<DocumentType> findAll();

    Optional<DocumentType> findByTitle(String title);

    List<DocumentType> findAllByIsActive(Boolean isActive);
    Page<DocumentType> findAllByIsActive(Pageable pageable, Boolean isActive);
    Optional<DocumentType> findByIdAndIsActive(Long id, Boolean isActive);


}
