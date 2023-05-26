package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.dto.PolicyDto;
import com.gitlab.alura.insuranceagency.entity.Document;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import com.gitlab.alura.insuranceagency.entity.Offer;
import com.gitlab.alura.insuranceagency.entity.Policy;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.InvalidInputException;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.filter.PolicyFilter;
import com.gitlab.alura.insuranceagency.mapper.PolicyMapper;
import com.gitlab.alura.insuranceagency.repository.PolicyRepository;
import com.gitlab.alura.insuranceagency.service.DocumentService;
import com.gitlab.alura.insuranceagency.service.OfferService;
import com.gitlab.alura.insuranceagency.service.PolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PolicyServiceImpl implements PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);
    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    private final UserDetailsServiceImpl userDetailsService;
    private final DocumentService documentService;

    private final OfferService offerService;



    public PolicyServiceImpl(PolicyRepository policyRepository,
                             PolicyMapper policyMapper,
                             UserDetailsServiceImpl userDetailsService,
                             DocumentService documentService, OfferService offerService) {
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.userDetailsService = userDetailsService;
        this.documentService = documentService;
        this.offerService = offerService;
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
    public void updatePolicy(Long id, PolicyDto policyDto) throws InvalidInputException{
        logger.info("Updating policy with ID: {}", id);
        Policy policy = getUpdatedPolicy(id, policyDto);

        Date currentDate = new Date();
        setNewPolicyInfo(policyDto, currentDate, policy);
        policy = policyRepository.save(policy);
        logger.info("Policy updated successfully: {}", policy.getId());
    }

    @Override
    public void handleApplication(String manager, String action, Long applicationId) {
        logger.info("Handling application. Manager: {}, Action: {}, Application ID: {}", manager, action, applicationId);
        Policy policy = policyRepository.findByIdAndIsActive(applicationId, true)
                .orElseThrow(IllegalArgumentException::new);
        policy.setManager(userDetailsService.getByEmail(manager));
        if (policy.getStartDate() == null){
            policy.setStartDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        }
        if (action.equals("approve")){
            policy.setApproved(true);
        }
        policyRepository.save(policy);
        logger.info("Application handled successfully.");
    }

    @Override
    public PolicyDto getApplicationForm(String clientEmail, Long offerId) {
        Policy application = new Policy();
        User client = userDetailsService.getByEmail(clientEmail);
        Offer offer = offerService.getById(offerId);

        application.setActive(true);
        application.setClient(client);
        application.setOffer(offer);


        return policyMapper.toPolicyDto(application);
    }

    @Override
    public Long createApplication(String clientEmail, PolicyDto policyDto, Long offerId) {
        logger.info("Creating application for client: {}, offer: {}", clientEmail, offerId);
        User client = userDetailsService.getByEmail(clientEmail);
        Offer offer = offerService.getById(offerId);
        Date currentDate = new Date();

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);

        PolicyDto existingPolicyDto = policyMapper.toPolicyDto(policy);
        policyMapper.updatePolicyDtoFromExisting(policyDto, existingPolicyDto);

        setNewPolicyInfo(policyDto, currentDate, policy);

        Long policyId = policyRepository.save(policy).getId();
        logger.info("Application created with ID: {}", policyId);
        return policyId;
    }

    private void setNewPolicyInfo(PolicyDto policyDto, Date currentDate, Policy policy) throws InvalidInputException{
        validatePolicyDate(policyDto, currentDate);
        policy.setStartDate(policyDto.getStartDate());

        if (policyDto.getDocuments() != null) {
            setPolicyDocuments(policyDto, policy, currentDate);
        }
    }

    private Policy getUpdatedPolicy(Long id, PolicyDto policyDto) {
        Policy policy = policyRepository.findByIdAndIsActive(id, true)
                .orElseThrow(NotFoundException::new);

        PolicyDto existingPolicyDto = policyMapper.toPolicyDto(policy);
        policyMapper.updatePolicyDtoFromExisting(policyDto, existingPolicyDto);
        return policy;
    }

    private void validatePolicyDate(PolicyDto policyDto, Date currentDate) throws InvalidInputException{
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

    private void processDocument(Date currentDate,
                                 Set<Document> documents,
                                 Map.Entry<DocumentType, Document> documentEntry) {
        documentEntry.getValue().setDocumentType(documentEntry.getKey());
        validateDocumentDate(currentDate, documentEntry);
        if (!documents.contains(documentEntry.getValue())) {
            documentService.addDocument(documentEntry.getValue());
        }
        documents.add(documentEntry.getValue());
    }

    private void validateDocumentDate(Date currentDate, Map.Entry<DocumentType, Document> documentEntry) throws InvalidInputException{
        if (documentEntry.getValue().getIssueDate().after(currentDate)) {
            throw new InvalidInputException(
                    String.format("documents[%d].issueDate", documentEntry.getKey().getId()),
                    "Issue date of document must be earlier than current date."
            );
        }
    }




    private PageImpl<PolicyDto> getPolicyByFilter(String email, boolean isPolicy, Pageable pageable) {
        User user = userDetailsService.getByEmail(email);

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
        User user = userDetailsService.getByEmail(email);

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
