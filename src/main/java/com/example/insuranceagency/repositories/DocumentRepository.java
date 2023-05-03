package com.example.insuranceagency.repositories;

import com.example.insuranceagency.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
