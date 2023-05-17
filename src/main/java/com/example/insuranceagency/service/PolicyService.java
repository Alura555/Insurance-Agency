package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.PolicyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PolicyService {
    Page<PolicyDto> getPoliciesByUser(String email, Pageable pageable);
    PolicyDto getPolicyByUserAndId(String email, Long id);

    Page<PolicyDto> getApplicationsByUser(String email, Pageable pageable);

    PolicyDto getApplicationByUserAndId(String email, Long id);

    void updatePolicy(Long id, PolicyDto policyDto);

    void handleApplication(String manager, String action, Long applicationId);
}
