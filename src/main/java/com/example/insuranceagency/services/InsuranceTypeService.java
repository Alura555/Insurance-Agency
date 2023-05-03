package com.example.insuranceagency.services;

import com.example.insuranceagency.dtos.InsuranceTypeDto;
import com.example.insuranceagency.entities.InsuranceType;

import java.util.List;

public interface InsuranceTypeService {
    public List<InsuranceTypeDto> getInsuranceTypes();
    public List<InsuranceTypeDto> getPopularInsuranceTypes(int n);
}
