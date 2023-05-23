package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.CompanyDto;
import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {

    List<CompanyDto> getActiveCompanies();

    List<Company> getPopularCompanies(int n);

    Company getCompanyByManager(User user);

    Page<CompanyDto> getAllActive(Pageable pageable);

    CompanyDto getCompanyDtoById(Long id);

    Long createNewCompany(CompanyDto companyDto);

    void deleteById(Long id);

    Long updateCompany(CompanyDto companyDto);

    void addCompanyManager(User manager, Long companyId);
}

