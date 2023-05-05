package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.InsuranceTypeDto;

import java.util.List;

public interface InsuranceTypeService {
    List<InsuranceTypeDto> getInsuranceTypes();
    List<InsuranceTypeDto> getPopularInsuranceTypes(int n);
}
