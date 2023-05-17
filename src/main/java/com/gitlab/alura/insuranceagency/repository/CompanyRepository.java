package com.gitlab.alura.insuranceagency.repository;


import com.gitlab.alura.insuranceagency.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByIsActive(Boolean isActive);
}
