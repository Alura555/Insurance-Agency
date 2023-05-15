package com.example.insuranceagency.service.implementation;

import com.example.insuranceagency.dto.PolicyDto;
import com.example.insuranceagency.entity.Document;
import com.example.insuranceagency.entity.Policy;
import com.example.insuranceagency.entity.User;
import com.example.insuranceagency.exception.InvalidInputException;
import com.example.insuranceagency.exception.NotFoundException;
import com.example.insuranceagency.filter.PolicyFilter;
import com.example.insuranceagency.mapper.PolicyMapper;
import com.example.insuranceagency.repository.PolicyRepository;
import com.example.insuranceagency.service.DocumentService;
import com.example.insuranceagency.service.PolicyService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    private final UserDetailsServiceImpl userDetailsService;
    private final DocumentService documentService;


    public PolicyServiceImpl(PolicyRepository policyRepository,
                             PolicyMapper policyMapper,
                             UserDetailsServiceImpl userDetailsService,
                             DocumentService documentService) {
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.userDetailsService = userDetailsService;
        this.documentService = documentService;
    }

    @Override
    public Page<PolicyDto> getPoliciesByUser(String email, int page, int size) {
        return getPolicyByFilter(email, true, page, size);
    }

    @Override
    public PolicyDto getPolicyByUserAndId(String email, Long id) {
        return getPolicyDtoByIdAndUser(email, true, id);
    }

    private PolicyDto getPolicyDtoByIdAndUser(String email, boolean isPolicy, Long id) {
        User user = userDetailsService.findByEmail(email);

        PolicyFilter policyFilter = new PolicyFilter();
        policyFilter.setActive(true);
        policyFilter.setPolicy(isPolicy);
        policyFilter.setUser(user);
        policyFilter.setId(id);

        Policy policy = policyRepository.findOne(policyFilter)
                .orElseThrow(NotFoundException::new);

        return policyMapper.toPolicyDto(policy);
    }

    @Override
    public Page<PolicyDto> getApplicationsByUser(String email, int page, int size) {
        return getPolicyByFilter(email, false, page, size);
    }

    @Override
    public PolicyDto getApplicationByUserAndId(String email, Long id) {
        return getPolicyDtoByIdAndUser(email, false, id);
    }

    @Override
    public void updatePolicy(Long id, PolicyDto policyDto) {
        Policy policy = policyRepository.findById(id).orElseThrow(NotFoundException::new);

        PolicyDto existingPolicyDto = policyMapper.toPolicyDto(policy);
        policyMapper.updatePolicyDtoFromExisting(policyDto, existingPolicyDto);

        Date currentDate = new Date();
        if (policyDto.getStartDate() != null && policyDto.getStartDate().before(currentDate)) {
            throw new InvalidInputException("startDate", "Start date must be later than current date.");
        }
        policy.setStartDate(policyDto.getStartDate());

        if (policyDto.getDocuments() != null) {
            Set<Document> documents = policy.getDocuments();
            policyDto.getDocuments()
                    .entrySet()
                    .stream()
                    .filter(x -> !x.getValue().getNumber().equals(""))
                    .forEach(x -> {
                        x.getValue().setDocumentType(x.getKey());
                        if (x.getValue().getIssueDate().after(currentDate)) {
                            throw new InvalidInputException("documents[" + x.getKey().getId() + "].issueDate",
                                    "Issue date of document must be earlier than current date.");
                        }
                        if (!documents.contains(x.getValue())) {
                            documentService.addNewDocument(x.getValue());
                        }
                        documents.add(x.getValue());
                    });
            policy.setDocuments(documents);
        }
        policyRepository.save(policy);
    }

    private PageImpl<PolicyDto> getPolicyByFilter(String email, boolean isPolicy, int page, int size) {
        User user = userDetailsService.findByEmail(email);

        PolicyFilter policyFilter = new PolicyFilter();
        policyFilter.setActive(true);
        policyFilter.setPolicy(isPolicy);
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
