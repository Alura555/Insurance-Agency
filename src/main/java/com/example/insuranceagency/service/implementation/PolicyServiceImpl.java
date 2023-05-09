package com.example.insuranceagency.service.implementation;

import com.example.insuranceagency.dto.OfferDto;
import com.example.insuranceagency.dto.PolicyDto;
import com.example.insuranceagency.entity.Offer;
import com.example.insuranceagency.entity.Policy;
import com.example.insuranceagency.entity.User;
import com.example.insuranceagency.filter.PolicyFilter;
import com.example.insuranceagency.mapper.PolicyMapper;
import com.example.insuranceagency.repository.PolicyRepository;
import com.example.insuranceagency.service.PolicyService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    private final UserDetailsServiceImpl userDetailsService;


    public PolicyServiceImpl(PolicyRepository policyRepository,
                             PolicyMapper policyMapper, UserDetailsServiceImpl userDetailsService) {
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Page<PolicyDto> getPoliciesByUser(String email, int page, int size) {
        User user = userDetailsService.findByEmail(email);

        PolicyFilter policyFilter = new PolicyFilter();
        policyFilter.setActive(true);
        policyFilter.setPolicy(true);
        policyFilter.setUser(user);

        Sort sort = Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Policy> policies = policyRepository.findAll(policyFilter, pageable);

        List<PolicyDto> offerDtoList = policies
                .getContent()
                .stream()
                .map(policyMapper::toPolicyDto)
                .collect(Collectors.toList());
        return new PageImpl<>(offerDtoList, pageable, policies.getTotalElements());
    }
}
