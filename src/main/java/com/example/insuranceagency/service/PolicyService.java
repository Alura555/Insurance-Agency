package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.PolicyDto;
import org.springframework.data.domain.Page;

public interface PolicyService {
    Page<PolicyDto> getPoliciesByUser(String email, int page, int size);

    Page<PolicyDto> getApplicationsByUser(String email, int page, int size);
}
