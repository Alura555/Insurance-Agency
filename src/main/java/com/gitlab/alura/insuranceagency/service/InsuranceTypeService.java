package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InsuranceTypeService {
    List<InsuranceTypeDto> getInsuranceTypes();
    List<InsuranceTypeDto> getPopularInsuranceTypes(int n);

    InsuranceType getById(Long id);
    InsuranceTypeDto getDtoById(Long id);

    Page<InsuranceTypeDto> getAllActive(Pageable pageable);

    void deleteById(Long id);

    void createNewInsuranceType(InsuranceTypeDto insuranceType);

    void updateInsuranceType(InsuranceTypeDto insuranceTypeDto);
}
