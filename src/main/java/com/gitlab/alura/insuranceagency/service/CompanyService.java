package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getActiveCompanies();

    List<Company> getPopularCompanies(int n);

}
