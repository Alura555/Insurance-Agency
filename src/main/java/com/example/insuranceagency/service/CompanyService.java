package com.example.insuranceagency.service;

import com.example.insuranceagency.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getActiveCompanies();

    List<Company> getPopularCompanies(int n);

}
