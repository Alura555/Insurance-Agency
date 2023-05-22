package com.gitlab.alura.insuranceagency.repository;


import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByIsActive(Boolean isActive);
    Page<Company> findAllByIsActive(Pageable pageable, Boolean isActive);
    Company findByManagers(User user);
}
