package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentTypeService {
    List<DocumentType> getAllActive();

    Page<DocumentTypeDto> getAllActive(Pageable pageable);

    void deleteById(Long id);

    DocumentTypeDto getById(Long id);

    Long updateDocumentType(DocumentTypeDto documentType);

    Long createNewDocumentType(DocumentTypeDto documentType);
}
