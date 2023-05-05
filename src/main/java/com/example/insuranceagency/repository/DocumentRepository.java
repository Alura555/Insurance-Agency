package com.example.insuranceagency.repository;

import com.example.insuranceagency.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
