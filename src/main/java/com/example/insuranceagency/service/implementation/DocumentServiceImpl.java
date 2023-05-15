package com.example.insuranceagency.service.implementation;


import com.example.insuranceagency.entity.Document;
import com.example.insuranceagency.repository.DocumentRepository;
import com.example.insuranceagency.service.DocumentService;
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
