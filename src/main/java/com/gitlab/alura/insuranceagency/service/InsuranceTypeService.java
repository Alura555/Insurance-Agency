package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;

import java.util.List;

public interface InsuranceTypeService {
    List<InsuranceTypeDto> getInsuranceTypes();
    List<InsuranceTypeDto> getPopularInsuranceTypes(int n);
}
