package com.example.insuranceagency.service.implementation;

import com.example.insuranceagency.dto.PolicyDto;
import com.example.insuranceagency.entity.Document;
import com.example.insuranceagency.entity.DocumentType;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public Page<PolicyDto> getPoliciesByUser(String email, Pageable pageable) {
        return getPolicyByFilter(email, true, pageable);
    }

    @Override
    public PolicyDto getPolicyByUserAndId(String email, Long id) {
        return getPolicyDtoByIdAndUser(email, true, id);
    }

    @Override
    public Page<PolicyDto> getApplicationsByUser(String email, Pageable pageable) {
        return getPolicyByFilter(email, false, pageable);
    }

    @Override
    public PolicyDto getApplicationByUserAndId(String email, Long id) {
        return getPolicyDtoByIdAndUser(email, false, id);
    }

    @Override
    public void updatePolicy(Long id, PolicyDto policyDto) {
        Policy policy = getUpdatedPolicy(id, policyDto);

        Date currentDate = new Date();
        validatePolicyDate(policyDto, currentDate);
        policy.setStartDate(policyDto.getStartDate());

        if (policyDto.getDocuments() != null) {
            setPolicyDocuments(policyDto, policy, currentDate);
        }
        policyRepository.save(policy);
    }

    private Policy getUpdatedPolicy(Long id, PolicyDto policyDto) {
        Policy policy = policyRepository.findById(id).orElseThrow(NotFoundException::new);

        PolicyDto existingPolicyDto = policyMapper.toPolicyDto(policy);
        policyMapper.updatePolicyDtoFromExisting(policyDto, existingPolicyDto);
        return policy;
    }

    private void validatePolicyDate(PolicyDto policyDto, Date currentDate) {
        if (policyDto.getStartDate() != null && policyDto.getStartDate().before(currentDate)) {
            throw new InvalidInputException("startDate", "Start date must be later than current date.");
        }
    }

    private void setPolicyDocuments(PolicyDto policyDto, Policy policy, Date currentDate) {
        Set<Document> documents = policy.getDocuments();
        policyDto.getDocuments()
                .entrySet()
                .stream()
                .filter(documentEntry -> !documentEntry.getValue().getNumber().equals(""))
                .forEach(documentEntry -> processDocument(currentDate, documents, documentEntry));
        policy.setDocuments(documents);
    }

    private void processDocument(Date currentDate, Set<Document> documents, Map.Entry<DocumentType, Document> documentEntry) {
        documentEntry.getValue().setDocumentType(documentEntry.getKey());
        validateDocumentDate(currentDate, documentEntry);
        if (!documents.contains(documentEntry.getValue())) {
            documentService.addNewDocument(documentEntry.getValue());
        }
        documents.add(documentEntry.getValue());
    }

    private void validateDocumentDate(Date currentDate, Map.Entry<DocumentType, Document> documentEntry) {
        if (documentEntry.getValue().getIssueDate().after(currentDate)) {
            throw new InvalidInputException(
                    String.format("documents[%d].issueDate", documentEntry.getKey().getId()),
                    "Issue date of document must be earlier than current date."
            );
        }
    }


    @Override
    public void handleApplication(String manager, String action, Long applicationId) {
        Policy policy = policyRepository.findById(applicationId).orElseThrow(IllegalArgumentException::new);
        policy.setManager(userDetailsService.findByEmail(manager));
        if (policy.getStartDate() == null){
            policy.setStartDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        }
        if (action.equals("approve")){
            policy.setApproved(true);
        }
        policyRepository.save(policy);
    }

    private PageImpl<PolicyDto> getPolicyByFilter(String email, boolean isPolicy, Pageable pageable) {
        User user = userDetailsService.findByEmail(email);

        PolicyFilter policyFilter = new PolicyFilter();
        policyFilter.setActive(true);
        policyFilter.setPolicy(isPolicy);
        policyFilter.setUser(user);

        Page<Policy> policies = policyRepository.findAll(policyFilter, pageable);

        List<PolicyDto> offerDtoList = policies
                .getContent()
                .stream()
                .map(policyMapper::toPolicyDto)
                .collect(Collectors.toList());
        return new PageImpl<>(offerDtoList, pageable, policies.getTotalElements());
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
}
