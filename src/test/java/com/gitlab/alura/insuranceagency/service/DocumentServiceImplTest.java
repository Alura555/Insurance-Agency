package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.entity.Document;
import com.gitlab.alura.insuranceagency.repository.DocumentRepository;
import com.gitlab.alura.insuranceagency.service.implementation.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest extends BaseClassTest {

    @Mock
    private DocumentRepository documentRepository;
    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test
    void addDocument_shouldSaveDocumentAndReturnId() {
        final String documentNumber = "ABC123";

        Document document = new Document();
        document.setNumber(documentNumber);

        when(documentRepository.save(document)).thenReturn(document);
        documentService.addDocument(document);

        verify(documentRepository).save(document);
    }
}
