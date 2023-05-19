package com.gitlab.alura.insuranceagency.repository;

import com.gitlab.alura.insuranceagency.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
