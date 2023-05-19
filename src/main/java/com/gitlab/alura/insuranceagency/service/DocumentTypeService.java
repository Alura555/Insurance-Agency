package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.entity.DocumentType;

import java.util.List;

public interface DocumentTypeService {
    List<DocumentType> getAllActive();
}
