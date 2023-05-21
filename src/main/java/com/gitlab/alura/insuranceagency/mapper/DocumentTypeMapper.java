package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import org.mapstruct.Mapper;

@Mapper
public interface DocumentTypeMapper {
    DocumentTypeDto toDocumentTypeDto(DocumentType documentType);
    DocumentType toDocumentType(DocumentTypeDto documentType);
}
