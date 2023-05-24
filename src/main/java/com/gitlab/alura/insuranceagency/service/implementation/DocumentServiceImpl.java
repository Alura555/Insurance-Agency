package com.gitlab.alura.insuranceagency.service.implementation;


import com.gitlab.alura.insuranceagency.entity.Document;
import com.gitlab.alura.insuranceagency.repository.DocumentRepository;
import com.gitlab.alura.insuranceagency.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void addDocument(Document document) {
        logger.info("Adding new document with number: {}", document.getNumber());
        document = documentRepository.save(document);
        logger.info("New document added successfully with ID: {}", document.getId());
    }
}
