package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.DocumentTypeMapper;
import com.gitlab.alura.insuranceagency.repository.DocumentTypeRepository;
import com.gitlab.alura.insuranceagency.service.implementation.DocumentTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class DocumentTypeServiceImplTest extends BaseClassTest {

    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private DocumentTypeMapper documentTypeMapper;
    @InjectMocks
    private DocumentTypeServiceImpl documentTypeService;

    @Test
    void getAllActive() {
        DocumentType type1 = new DocumentType();
        type1.setTitle(DOCUMENT_TITLE_1);
        DocumentType type2 = new DocumentType();
        type2.setTitle(DOCUMENT_TITLE_2);
        DocumentType type3 = new DocumentType();
        type3.setTitle(DOCUMENT_TITLE_3);
        List<DocumentType> expectedDocumentTypes = Arrays.asList(
                type1,
                type2,
                type3
        );

        when(documentTypeRepository.findAllByIsActive(true)).thenReturn(expectedDocumentTypes);

        List<DocumentType> actualDocumentTypes = documentTypeService.getAllActive();

        assertEquals(expectedDocumentTypes, actualDocumentTypes);
        verify(documentTypeRepository, times(1)).findAllByIsActive(true);
    }

    @Test
    void getAllActiveByPage() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        DocumentType type1 = new DocumentType();
        type1.setTitle(DOCUMENT_TITLE_1);
        DocumentType type2 = new DocumentType();
        type2.setTitle(DOCUMENT_TITLE_2);
        DocumentType type3 = new DocumentType();
        type3.setTitle(DOCUMENT_TITLE_3);
        List<DocumentType> documentTypes = Arrays.asList(
                type1,
                type2,
                type3
        );

        Page<DocumentType> documentTypePage = new PageImpl<>(documentTypes, pageable, documentTypes.size());

        DocumentTypeDto typeDto1 = new DocumentTypeDto();
        typeDto1.setTitle(DOCUMENT_TITLE_1);
        DocumentTypeDto typeDto2 = new DocumentTypeDto();
        typeDto2.setTitle(DOCUMENT_TITLE_2);
        DocumentTypeDto typeDto3 = new DocumentTypeDto();
        typeDto3.setTitle(DOCUMENT_TITLE_3);
        List<DocumentTypeDto> expectedDocumentTypeDtos = Arrays.asList(
                typeDto1,
                typeDto2,
                typeDto3
        );

        when(documentTypeRepository.findAllByIsActive(pageable, true)).thenReturn(documentTypePage);
        when(documentTypeMapper.toDocumentTypeDto(type1)).thenReturn(typeDto1);
        when(documentTypeMapper.toDocumentTypeDto(type2)).thenReturn(typeDto2);
        when(documentTypeMapper.toDocumentTypeDto(type3)).thenReturn(typeDto3);

        Page<DocumentTypeDto> actualDocumentTypeDtos = documentTypeService.getAllActive(pageable);

        assertEquals(expectedDocumentTypeDtos, actualDocumentTypeDtos.getContent());
        assertEquals(documentTypePage.getTotalElements(), actualDocumentTypeDtos.getTotalElements());
        verify(documentTypeRepository, times(1)).findAllByIsActive(pageable, true);
        verify(documentTypeMapper, times(3)).toDocumentTypeDto(any(DocumentType.class));
    }

    @Test
    void deleteById() {
        DocumentType documentType = new DocumentType();
        documentType.setId(DOCUMENT_TYPE_ID);
        documentType.setActive(true);

        when(documentTypeRepository.findByIdAndIsActive(DOCUMENT_TYPE_ID, true))
                .thenReturn(Optional.of(documentType));
        documentTypeService.deleteById(DOCUMENT_TYPE_ID);

        assertFalse(documentType.getActive());
        verify(documentTypeRepository, times(1)).save(documentType);
    }

    @Test
    void deleteByNonExistingId() {
        when(documentTypeRepository.findByIdAndIsActive(DOCUMENT_TYPE_ID, true))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> documentTypeService.deleteById(DOCUMENT_TYPE_ID)
        );
        verify(documentTypeRepository, times(0)).save(any(DocumentType.class));
    }

    @Test
    void getDtoById() {
        DocumentType documentType = new DocumentType();
        documentType.setId(DOCUMENT_TYPE_ID);

        DocumentTypeDto expectedDocumentTypeDto = new DocumentTypeDto();
        expectedDocumentTypeDto.setId(1L);

        when(documentTypeRepository.findByIdAndIsActive(DOCUMENT_TYPE_ID, true))
                .thenReturn(Optional.of(documentType));
        when(documentTypeMapper.toDocumentTypeDto(documentType))
                .thenReturn(expectedDocumentTypeDto);

        DocumentTypeDto actualDocumentTypeDto = documentTypeService.getDtoById(DOCUMENT_TYPE_ID);

        assertEquals(expectedDocumentTypeDto, actualDocumentTypeDto);
        verify(documentTypeRepository, times(1)).findByIdAndIsActive(DOCUMENT_TYPE_ID, true);
        verify(documentTypeMapper, times(1)).toDocumentTypeDto(documentType);
    }

    @Test
    void getDtoByNonExistingId() {
        when(documentTypeRepository.findByIdAndIsActive(DOCUMENT_TYPE_ID, true))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> documentTypeService.getDtoById(DOCUMENT_TYPE_ID));
        verify(documentTypeRepository, times(1)).findByIdAndIsActive(DOCUMENT_TYPE_ID, true);
        verify(documentTypeMapper, times(0)).toDocumentTypeDto(any(DocumentType.class));
    }

    @Test
    void updateDocumentType() {
        DocumentType documentType = new DocumentType();
        documentType.setId(DOCUMENT_TYPE_ID);

        DocumentTypeDto documentTypeDto = new DocumentTypeDto();
        documentTypeDto.setId(DOCUMENT_TYPE_ID);

        when(documentTypeRepository.findByIdAndIsActive(DOCUMENT_TYPE_ID, true))
                .thenReturn(Optional.of(documentType));
        when(documentTypeMapper.toDocumentType(documentTypeDto)).thenReturn(documentType);
        when(documentTypeRepository.save(documentType)).thenReturn(documentType);

        documentTypeService.updateDocumentType(documentTypeDto);

        verify(documentTypeMapper, times(1)).toDocumentType(documentTypeDto);
        verify(documentTypeRepository, times(2)).save(any(DocumentType.class));
    }


    @Test
    void createDocumentType() {
        DocumentTypeDto documentTypeDto = new DocumentTypeDto();
        DocumentType documentType = new DocumentType();
        documentType.setId(DOCUMENT_TYPE_ID);

        when(documentTypeMapper.toDocumentType(documentTypeDto)).thenReturn(documentType);
        when(documentTypeRepository.save(documentType)).thenReturn(documentType);

        Long actualId = documentTypeService.createDocumentType(documentTypeDto);

        assertEquals(documentType.getId(), actualId);

        verify(documentTypeMapper, times(1)).toDocumentType(documentTypeDto);
        verify(documentTypeRepository, times(1)).save(documentType);
    }
}
