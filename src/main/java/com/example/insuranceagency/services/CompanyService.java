package com.example.insuranceagency.services;

import com.example.insuranceagency.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CompanyService {

    List<Company> getActiveCompanies();

    List<Company> getPopularCompanies(int n);

}
