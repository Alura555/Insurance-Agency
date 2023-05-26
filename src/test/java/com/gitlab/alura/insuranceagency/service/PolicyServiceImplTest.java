package com.gitlab.alura.insuranceagency.service;

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
import com.gitlab.alura.insuranceagency.service.implementation.PolicyServiceImpl;
import com.gitlab.alura.insuranceagency.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceImplTest extends BaseClassTest {
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PolicyMapper policyMapper;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private DocumentService documentService;
    @Mock
    private OfferService offerService;
    @InjectMocks
    private PolicyServiceImpl policyService;

    @Test
    void testGetPoliciesByUserWithPolicies() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        User user = new User();
        user.setEmail(USER_EMAIL);

        List<Policy> policyList = new ArrayList<>();
        policyList.add(new Policy());
        policyList.add(new Policy());

        Page<Policy> policyPage = new PageImpl<>(policyList, pageable, policyList.size());

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findAll(any(PolicyFilter.class), eq(pageable))).thenReturn(policyPage);

        Page<PolicyDto> resultPage = policyService.getPoliciesByUser(USER_EMAIL, pageable);

        assertNotNull(resultPage);
        assertEquals(policyList.size(), resultPage.getContent().size());
        assertEquals(policyList.size(), resultPage.getTotalElements());
        verify(userDetailsService, times(1)).getByEmail(USER_EMAIL);
        verify(policyRepository, times(1)).findAll(any(PolicyFilter.class), eq(pageable));
    }

    @Test
    void testGetPoliciesByUserWithNoPolicies() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        User user = new User();
        user.setEmail(USER_EMAIL);

        List<Policy> policyList = new ArrayList<>();

        Page<Policy> policyPage = new PageImpl<>(policyList, pageable, policyList.size());

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findAll(any(PolicyFilter.class), eq(pageable))).thenReturn(policyPage);

        Page<PolicyDto> resultPage = policyService.getPoliciesByUser(USER_EMAIL, pageable);

        assertNotNull(resultPage);
        assertTrue(resultPage.getContent().isEmpty());
        assertEquals(policyList.size(), resultPage.getTotalElements());
        verify(userDetailsService, times(1)).getByEmail(USER_EMAIL);
        verify(policyRepository, times(1)).findAll(any(PolicyFilter.class), eq(pageable));
    }

    @Test
    void testGetPoliciesByUserWithNullResult() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        User user = new User();
        user.setEmail(USER_EMAIL);

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findAll(any(PolicyFilter.class), eq(pageable))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> policyService.getPoliciesByUser(USER_EMAIL, pageable));

        verify(userDetailsService, times(1)).getByEmail(USER_EMAIL);
        verify(policyRepository, times(1)).findAll(any(PolicyFilter.class), eq(pageable));
    }

    @Test
    public void testGetPolicyByUserAndId() {
        User user = new User();
        user.setEmail(USER_EMAIL);

        Policy policy = new Policy();
        policy.setId(POLICY_ID);
        policy.setClient(user);

        PolicyDto expectedPolicyDto = new PolicyDto();
        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findOne(any(PolicyFilter.class))).thenReturn(Optional.of(policy));
        when(policyMapper.toPolicyDto(policy)).thenReturn(expectedPolicyDto);

        PolicyDto result = policyService.getPolicyByUserAndId(USER_EMAIL, POLICY_ID);

        assertEquals(expectedPolicyDto, result);
        verify(userDetailsService).getByEmail(USER_EMAIL);
        verify(policyRepository).findOne(any(PolicyFilter.class));
        verify(policyMapper).toPolicyDto(policy);
    }

    @Test
    public void testGetNonExistingPolicyByUserAndId() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findOne(any(PolicyFilter.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            policyService.getPolicyByUserAndId(USER_EMAIL, POLICY_ID);
        });
        verify(userDetailsService).getByEmail(USER_EMAIL);
        verify(policyRepository).findOne(any(PolicyFilter.class));
        verifyNoMoreInteractions(policyMapper);
    }

    @Test
    public void testGetApplicationsByUser() {
        User user = new User();
        user.setEmail(USER_EMAIL);

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        List<Policy> policies = List.of(new Policy(), new Policy());
        List<PolicyDto> expectedPolicyDtos = List.of(new PolicyDto(), new PolicyDto());
        Page<Policy> policyPage = new PageImpl<>(policies, pageable, 2);

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findAll(any(PolicyFilter.class), eq(pageable))).thenReturn(policyPage);
        when(policyMapper.toPolicyDto(policies.get(0))).thenReturn(expectedPolicyDtos.get(0));
        when(policyMapper.toPolicyDto(policies.get(1))).thenReturn(expectedPolicyDtos.get(1));

        Page<PolicyDto> result = policyService.getApplicationsByUser(USER_EMAIL, pageable);

        assertEquals(expectedPolicyDtos, result.getContent());
        assertEquals(2, result.getTotalElements());
        verify(userDetailsService).getByEmail(USER_EMAIL);
        verify(policyRepository).findAll(any(PolicyFilter.class), eq(pageable));
        verify(policyMapper, times(2)).toPolicyDto(any(Policy.class));
    }

    @Test
    public void testGetNonExistingApplicationsByUser() {
        User user = new User();
        user.setEmail(USER_EMAIL);

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        List<Policy> policies = Collections.emptyList();
        List<PolicyDto> expectedPolicyDtos = Collections.emptyList();
        Page<Policy> policyPage = new PageImpl<>(policies, pageable, 0);

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findAll(any(PolicyFilter.class), eq(pageable))).thenReturn(policyPage);

        Page<PolicyDto> result = policyService.getApplicationsByUser(USER_EMAIL, pageable);

        assertEquals(expectedPolicyDtos, result.getContent());
        assertEquals(0, result.getTotalElements());
        verify(userDetailsService).getByEmail(USER_EMAIL);
        verify(policyRepository).findAll(any(PolicyFilter.class), eq(pageable));
        verifyNoInteractions(policyMapper);
    }

    @Test
    public void testGetApplicationByUserAndId() {
        User user = new User();
        user.setEmail(USER_EMAIL);

        Policy policy = new Policy();
        PolicyDto expectedPolicyDto = new PolicyDto();

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findOne(any(PolicyFilter.class))).thenReturn(Optional.of(policy));
        when(policyMapper.toPolicyDto(policy)).thenReturn(expectedPolicyDto);

        PolicyDto result = policyService.getApplicationByUserAndId(USER_EMAIL, APPLICATION_ID);

        assertEquals(expectedPolicyDto, result);
        verify(userDetailsService).getByEmail(USER_EMAIL);
        verify(policyRepository).findOne(any(PolicyFilter.class));
        verify(policyMapper).toPolicyDto(policy);
    }

    @Test
    public void testGetNonExistingApplicationByUserAndId() {
        User user = new User();
        user.setEmail(USER_EMAIL);

        when(userDetailsService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(policyRepository.findOne(any(PolicyFilter.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            policyService.getApplicationByUserAndId(USER_EMAIL, APPLICATION_ID);
        });
        verify(userDetailsService).getByEmail(USER_EMAIL);
        verify(policyRepository).findOne(any(PolicyFilter.class));
        verifyNoMoreInteractions(policyMapper);
    }

    @Test
    public void testHandleApplication_WhenApplicationExistsAndActionIsApprove_ShouldSetManagerAndStartDateAndApproved() {
        Policy policy = new Policy();
        User manager = new User();
        manager.setEmail(MANAGER_EMAIL);
        policy.setId(APPLICATION_ID);
        when(policyRepository.findByIdAndIsActive(APPLICATION_ID, true)).thenReturn(Optional.of(policy));
        when(userDetailsService.getByEmail(MANAGER_EMAIL)).thenReturn(manager);

        policyService.handleApplication(MANAGER_EMAIL, ACTION_APPROVE, APPLICATION_ID);

        assertEquals(manager, policy.getManager());
        assertNotNull(policy.getStartDate());
        assertTrue(policy.isApproved());
        verify(policyRepository).findByIdAndIsActive(APPLICATION_ID, true);
        verify(userDetailsService).getByEmail(MANAGER_EMAIL);
        verify(policyRepository).save(policy);
    }

    @Test
    public void testHandleApplicationWithReject() {
        Policy policy = new Policy();
        User manager = new User();
        manager.setEmail(MANAGER_EMAIL);
        policy.setId(APPLICATION_ID);
        when(policyRepository.findByIdAndIsActive(APPLICATION_ID, true)).thenReturn(Optional.of(policy));
        when(userDetailsService.getByEmail(MANAGER_EMAIL)).thenReturn(manager);

        policyService.handleApplication(MANAGER_EMAIL, ACTION_REJECT, APPLICATION_ID);

        assertEquals(manager, policy.getManager());
        assertNotNull(policy.getStartDate());
        assertFalse(policy.isApproved());
        verify(policyRepository).findByIdAndIsActive(APPLICATION_ID, true);
        verify(userDetailsService).getByEmail(MANAGER_EMAIL);
        verify(policyRepository).save(policy);
    }

    @Test
    public void testHandleNonExistingApplication() {
        when(policyRepository.findByIdAndIsActive(APPLICATION_ID, true)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            policyService.handleApplication(MANAGER_EMAIL, ACTION_APPROVE, APPLICATION_ID);
        });

        verify(policyRepository).findByIdAndIsActive(APPLICATION_ID, true);
        verifyNoInteractions(userDetailsService);
        verify(policyRepository, times(0)).save(any(Policy.class));
    }

    @Test
    public void testCreateApplication() {
        Date currentDate = new Date();

        Document document = new Document();
        document.setIssueDate(currentDate);
        document.setNumber(DOCUMENT_NUMBER);

        PolicyDto policyDto = new PolicyDto();
        policyDto.setDocuments(Map.of(new DocumentType(), document));

        PolicyDto existingPolicyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);
        policy.setDocuments(new HashSet<>());

        when(userDetailsService.getByEmail(CLIENT_EMAIL)).thenReturn(client);
        when(offerService.getById(OFFER_ID)).thenReturn(offer);
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);
        when(policyMapper.toPolicyDto(any(Policy.class))).thenReturn(existingPolicyDto);

        Long policyId = policyService.createApplication(CLIENT_EMAIL, policyDto, OFFER_ID);

        assertEquals(policy.getId(), policyId);
        verify(userDetailsService).getByEmail(CLIENT_EMAIL);
        verify(offerService).getById(OFFER_ID);
        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    public void testCreateApplicationWithInvalidStartDate() {
        Date currentDate = new Date();

        Document document = new Document();
        document.setIssueDate(currentDate);
        document.setNumber(DOCUMENT_NUMBER);

        PolicyDto policyDto = new PolicyDto();
        policyDto.setDocuments(Map.of(new DocumentType(), document));
        policyDto.setStartDate(addDaysToDate(currentDate, -5));
        PolicyDto existingPolicyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);
        policy.setDocuments(new HashSet<>());

        when(userDetailsService.getByEmail(CLIENT_EMAIL)).thenReturn(client);
        when(offerService.getById(OFFER_ID)).thenReturn(offer);
        when(policyMapper.toPolicyDto(any(Policy.class))).thenReturn(existingPolicyDto);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> policyService.createApplication(CLIENT_EMAIL, policyDto, OFFER_ID));

        assertEquals("Start date must be later than current date.", exception.getMessage());
        verify(userDetailsService).getByEmail(CLIENT_EMAIL);
        verify(offerService).getById(OFFER_ID);
        verify(policyRepository, times(0)).save(any(Policy.class));
    }

    @Test
    public void testCreateApplicationWithInvalidDocumentDate() {
        Date currentDate = new Date();

        Document document = new Document();
        document.setIssueDate(addDaysToDate(currentDate, 4));
        document.setNumber(DOCUMENT_NUMBER);

        PolicyDto policyDto = new PolicyDto();
        policyDto.setDocuments(Map.of(new DocumentType(), document));
        policyDto.setStartDate(addDaysToDate(currentDate, 5));
        PolicyDto existingPolicyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);
        policy.setDocuments(new HashSet<>());

        when(userDetailsService.getByEmail(CLIENT_EMAIL)).thenReturn(client);
        when(offerService.getById(OFFER_ID)).thenReturn(offer);
        when(policyMapper.toPolicyDto(any(Policy.class))).thenReturn(existingPolicyDto);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> policyService.createApplication(CLIENT_EMAIL, policyDto, OFFER_ID));

        assertEquals("Issue date of document must be earlier than current date.", exception.getMessage());
        verify(userDetailsService).getByEmail(CLIENT_EMAIL);
        verify(offerService).getById(OFFER_ID);
        verify(policyRepository, times(0)).save(any(Policy.class));
    }

    @Test
    public void testCreateApplicationWithNonExistingOffer() {
        Long offerId = 999L;
        PolicyDto policyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        when(userDetailsService.getByEmail(CLIENT_EMAIL)).thenReturn(client);
        when(offerService.getById(offerId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> policyService.createApplication(CLIENT_EMAIL, policyDto, offerId));


        verify(userDetailsService).getByEmail(CLIENT_EMAIL);
        verify(offerService).getById(offerId);
        verifyNoInteractions(policyRepository);
    }

    @Test
    public void testUpdatePolicy() {
        Date currentDate = new Date();

        Document document = new Document();
        document.setIssueDate(currentDate);
        document.setNumber(DOCUMENT_NUMBER);

        PolicyDto policyDto = new PolicyDto();
        policyDto.setDocuments(Map.of(new DocumentType(), document));

        PolicyDto existingPolicyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);
        policy.setDocuments(new HashSet<>());

        when(policyRepository.findByIdAndIsActive(POLICY_ID, true)).thenReturn(Optional.of(policy));
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);
        when(policyMapper.toPolicyDto(any(Policy.class))).thenReturn(existingPolicyDto);

        policyService.updatePolicy(POLICY_ID, policyDto);

        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    public void testUpdatePolicyWithInvalidStartDate() {
        Date currentDate = new Date();

        Document document = new Document();
        document.setIssueDate(currentDate);
        document.setNumber(DOCUMENT_NUMBER);

        PolicyDto policyDto = new PolicyDto();
        policyDto.setDocuments(Map.of(new DocumentType(), document));
        policyDto.setStartDate(addDaysToDate(currentDate, -5));
        PolicyDto existingPolicyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);
        policy.setDocuments(new HashSet<>());

        when(policyRepository.findByIdAndIsActive(POLICY_ID, true)).thenReturn(Optional.of(policy));
        when(policyMapper.toPolicyDto(any(Policy.class))).thenReturn(existingPolicyDto);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> policyService.updatePolicy(POLICY_ID, policyDto));

        assertEquals("Start date must be later than current date.", exception.getMessage());
        verify(policyRepository, times(0)).save(any(Policy.class));
    }

    @Test
    public void testUpdatePolicyWithInvalidDocumentDate() {
        Date currentDate = new Date();

        Document document = new Document();
        document.setIssueDate(addDaysToDate(currentDate, 4));
        document.setNumber(DOCUMENT_NUMBER);

        PolicyDto policyDto = new PolicyDto();
        policyDto.setDocuments(Map.of(new DocumentType(), document));
        policyDto.setStartDate(addDaysToDate(currentDate, 5));
        PolicyDto existingPolicyDto = new PolicyDto();
        User client = new User();
        client.setEmail(CLIENT_EMAIL);
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        Policy policy = new Policy();
        policy.setActive(true);
        policy.setClient(client);
        policy.setOffer(offer);
        policy.setCreationDate(currentDate);
        policy.setDocuments(new HashSet<>());

        when(policyRepository.findByIdAndIsActive(POLICY_ID, true)).thenReturn(Optional.of(policy));
        when(policyMapper.toPolicyDto(any(Policy.class))).thenReturn(existingPolicyDto);

        Exception exception = assertThrows(InvalidInputException.class,
                () -> policyService.updatePolicy(POLICY_ID, policyDto));

        assertEquals("Issue date of document must be earlier than current date.", exception.getMessage());
        verify(policyRepository, times(0)).save(any(Policy.class));
    }

}
