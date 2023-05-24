package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.DocumentTypeMapper;
import com.gitlab.alura.insuranceagency.repository.DocumentTypeRepository;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

    private final DocumentTypeRepository documentTypeRepository;

    private final DocumentTypeMapper documentTypeMapper;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository,
                                   DocumentTypeMapper documentTypeMapper) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
    }

    @Override
    public List<DocumentType> getAllActive() {
        return documentTypeRepository.findAllByIsActive(true);
    }

    @Override
    public Page<DocumentTypeDto> getAllActive(Pageable pageable) {
        Page<DocumentType> documentTypePage = documentTypeRepository.findAllByIsActive(pageable, true);
        List<DocumentTypeDto> documentTypeDtoList = documentTypePage
                .getContent()
                .stream()
                .map(documentTypeMapper::toDocumentTypeDto)
                .collect(Collectors.toList());
        return new PageImpl<>(documentTypeDtoList, pageable, documentTypePage.getTotalElements());
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting document type with ID: {}", id);
        DocumentType documentType = getById(id);
        documentType.setActive(false);
        documentTypeRepository.save(documentType);
        logger.info("Document type deleted successfully");
    }

    private DocumentType getById(Long id) {
        return documentTypeRepository.findByIdAndIsActive(id, true)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public DocumentTypeDto getDtoById(Long id) {
        DocumentType documentType = getById(id);
        return documentTypeMapper.toDocumentTypeDto(documentType);
    }

    @Override
    public Long updateDocumentType(DocumentTypeDto documentType) {
        deleteById(documentType.getId());

        documentType.setActive(true);
        documentType.setId(null);
        return createDocumentType(documentType);
    }

    @Override
    public Long createDocumentType(DocumentTypeDto documentTypeDto) {
        logger.info("Creating new document type");
        documentTypeDto.setActive(true);

        DocumentType documentType = documentTypeMapper.toDocumentType(documentTypeDto);
        documentType = documentTypeRepository.save(documentType);
        logger.info("New document type created successfully with ID: {}", documentType.getId());
        return documentType.getId();
    }
}
