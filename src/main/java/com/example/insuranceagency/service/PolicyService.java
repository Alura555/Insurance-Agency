package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.PolicyDto;
import org.springframework.data.domain.Page;

public interface PolicyService {
    Page<PolicyDto> getPoliciesByUser(String email, int page, int size);
    PolicyDto getPolicyByUserAndId(String email, Long id);

    Page<PolicyDto> getApplicationsByUser(String email, int page, int size);

    PolicyDto getApplicationByUserAndId(String email, Long id);

    void updatePolicy(Long id, PolicyDto policyDto);

    void handleApplication(String manager, String action, Long applicationId);
}
