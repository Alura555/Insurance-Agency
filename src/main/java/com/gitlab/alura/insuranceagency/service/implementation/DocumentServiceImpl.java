package com.gitlab.alura.insuranceagency.service.implementation;


import com.gitlab.alura.insuranceagency.entity.Document;
import com.gitlab.alura.insuranceagency.repository.DocumentRepository;
import com.gitlab.alura.insuranceagency.service.DocumentService;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void addNewDocument(Document document) {
        documentRepository.save(document);
    }
}
