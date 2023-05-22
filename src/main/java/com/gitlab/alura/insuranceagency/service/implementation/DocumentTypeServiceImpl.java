package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.DocumentTypeMapper;
import com.gitlab.alura.insuranceagency.repository.DocumentTypeRepository;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

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
        DocumentType documentType = getById(id);
        documentType.setActive(false);
        documentTypeRepository.save(documentType);
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
        return createNewDocumentType(documentType);
    }

    @Override
    public Long createNewDocumentType(DocumentTypeDto documentTypeDto) {
        documentTypeDto.setActive(true);

        DocumentType documentType = documentTypeMapper.toDocumentType(documentTypeDto);
        return documentTypeRepository.save(documentType).getId();
    }
}
