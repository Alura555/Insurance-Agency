package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;

import java.util.List;

public interface InsuranceTypeService {
    List<InsuranceTypeDto> getInsuranceTypes();
    List<InsuranceTypeDto> getPopularInsuranceTypes(int n);

    InsuranceType getById(Long id);
}
