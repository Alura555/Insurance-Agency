package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.entity.DocumentType;
import com.gitlab.alura.insuranceagency.repository.DocumentTypeRepository;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public List<DocumentType> getAllActive() {
        return documentTypeRepository.findAllByIsActive(true);
    }
}
