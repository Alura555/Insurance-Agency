package com.example.insuranceagency.services.implementation;

import com.example.insuranceagency.entities.Company;
import com.example.insuranceagency.exceptions.NotFoundException;
import com.example.insuranceagency.repositories.CompanyRepository;
import com.example.insuranceagency.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getActiveCompanies(){
        return companyRepository.findAllByIsActive(true);
    }

    @Override
    public List<Company> getPopularCompanies(int n) {
        if (n <= 0){
            throw new IllegalArgumentException();
        }
        return companyRepository.findAllByIsActive(true)
                .stream()
                .sorted(Comparator.comparing(Company::getActiveOffersCount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
}
