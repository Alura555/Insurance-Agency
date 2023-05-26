package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import com.gitlab.alura.insuranceagency.entity.Offer;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import com.gitlab.alura.insuranceagency.mapper.OfferMapper;
import com.gitlab.alura.insuranceagency.repository.OfferRepository;
import com.gitlab.alura.insuranceagency.service.implementation.OfferServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfferServiceImplTest extends BaseClassTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private UserService userService;
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    void testGetAllByFilter() {
        OfferFilter offerFilter = new OfferFilter();
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        List<Offer> offerList = Arrays.asList(new Offer(), new Offer(), new Offer());
        Page<Offer> offerPage = new PageImpl<>(offerList, pageable, 3);
        when(offerRepository.findAll(offerFilter, pageable)).thenReturn(offerPage);

        List<OfferDto> offerDtoList = Arrays.asList(new OfferDto(), new OfferDto(), new OfferDto());
        when(offerMapper.toOfferDto(any(Offer.class))).thenReturn(new OfferDto());
        when(offerMapper.toOfferDto(offerList.get(0))).thenReturn(offerDtoList.get(0));
        when(offerMapper.toOfferDto(offerList.get(1))).thenReturn(offerDtoList.get(1));
        when(offerMapper.toOfferDto(offerList.get(2))).thenReturn(offerDtoList.get(2));

        Page<OfferDto> result = offerService.getAll(pageable, offerFilter);

        assertEquals(offerDtoList, result.getContent());
        assertEquals(3, result.getTotalElements());

        verify(offerRepository, times(1)).findAll(offerFilter, pageable);
        verify(offerMapper, times(3)).toOfferDto(any(Offer.class));
    }

    @Test
    void testGetAllByFilterWithEmptyPage() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        when(offerRepository.findAll(any(OfferFilter.class), eq(pageable))).thenReturn(Page.empty());

        Page<OfferDto> resultPage = offerService.getAll(pageable, new OfferFilter());

        assertEquals(0, resultPage.getTotalElements());
        assertEquals(0, resultPage.getTotalPages());
        assertEquals(0, resultPage.getContent().size());
    }

    @Test
    void testGetAllByUserEmail() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        User user = new User();
        user.setEmail(USER_EMAIL);

        when(userService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(offerRepository.findAll(any(OfferFilter.class), eq(pageable))).thenReturn(Page.empty());

        Page<OfferDto> resultPage = offerService.getAll(pageable, USER_EMAIL);

        assertEquals(0, resultPage.getTotalElements());
        assertEquals(0, resultPage.getTotalPages());
        assertEquals(0, resultPage.getContent().size());
    }

    @Test
    void testGetAllWithOffers() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        List<Offer> offers = Arrays.asList(offer1, offer2);

        OfferDto offerDto1 = new OfferDto();
        OfferDto offerDto2 = new OfferDto();
        List<OfferDto> offerDtoList = Arrays.asList(offerDto1, offerDto2);

        when(userService.getByEmail(USER_EMAIL)).thenReturn(user);
        when(offerRepository.findAll(any(OfferFilter.class), eq(pageable)))
                .thenReturn(new PageImpl<>(offers, pageable, offers.size()));
        when(offerMapper.toOfferDto(offer1)).thenReturn(offerDto1);
        when(offerMapper.toOfferDto(offer2)).thenReturn(offerDto2);

        Page<OfferDto> resultPage = offerService.getAll(pageable, USER_EMAIL);

        assertEquals(2, resultPage.getTotalElements());
        assertEquals(1, resultPage.getTotalPages());
        assertEquals(offerDtoList, resultPage.getContent());
    }

    @Test
    void testGetDtoByIdWithExistingOffer() {
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        OfferDto expectedOfferDto = new OfferDto();
        expectedOfferDto.setId(OFFER_ID);

        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(offer));
        when(offerMapper.toOfferDto(offer)).thenReturn(expectedOfferDto);

        OfferDto actualOfferDto = offerService.getDtoById(OFFER_ID);

        assertEquals(expectedOfferDto, actualOfferDto);
    }

    @Test
    void testGetDtoByIdWithNonExistingOffer() {
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offerService.getDtoById(OFFER_ID));
    }

    @Test
    void testGetOfferByManagerAndIdWithExistingOffer() {
        User user = new User();
        user.setEmail(MANAGER_EMAIL);

        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        OfferDto expectedOfferDto = new OfferDto();
        expectedOfferDto.setId(OFFER_ID);

        when(userService.getByEmail(MANAGER_EMAIL)).thenReturn(user);
        when(offerRepository.findOne(any(OfferFilter.class))).thenReturn(Optional.of(offer));
        when(offerMapper.toOfferDto(offer)).thenReturn(expectedOfferDto);

        OfferDto actualOfferDto = offerService.getOfferByManagerAndId(MANAGER_EMAIL, OFFER_ID);

        assertEquals(expectedOfferDto, actualOfferDto);
    }

    @Test
    void testGetOfferByManagerAndIdWithNonExistingOffer() {
        User user = new User();
        user.setEmail(MANAGER_EMAIL);

        when(userService.getByEmail(MANAGER_EMAIL)).thenReturn(user);
        when(offerRepository.findOne(any(OfferFilter.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offerService.getOfferByManagerAndId(MANAGER_EMAIL, OFFER_ID));
    }

    @Test
    void testGetOfferByManagerAndIdWithNullManagerEmail() {
        assertThrows(NotFoundException.class, () -> offerService.getOfferByManagerAndId(null, OFFER_ID));
    }

    @Test
    void testGetByIdWithExistingOffer() {
        Offer expectedOffer = new Offer();
        expectedOffer.setId(OFFER_ID);

        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(expectedOffer));

        Offer actualOffer = offerService.getById(OFFER_ID);

        assertEquals(expectedOffer, actualOffer);
    }

    @Test
    void testGetByIdWithNonExistingOffer() {
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offerService.getById(OFFER_ID));
    }

    @Test
    void testGetByIdWithNullId() {
        assertThrows(NotFoundException.class, () -> offerService.getById(null));
    }

    @Test
    void testGetMaxPriceWithExistingOffers() {
        BigDecimal maxPrice = new BigDecimal("1000.00");
        Offer offer2 = new Offer();
        offer2.setPrice(maxPrice);

        when(offerRepository.findTopByOrderByPriceDesc()).thenReturn(offer2);

        BigDecimal actualMaxPrice = offerService.getMaxPrice();

        assertEquals(maxPrice, actualMaxPrice);
    }

    @Test
    void testGetMaxPriceWithNullPrice() {
        Offer offer = new Offer();
        offer.setPrice(null);

        when(offerRepository.findTopByOrderByPriceDesc()).thenReturn(offer);

        BigDecimal actualMaxPrice = offerService.getMaxPrice();

        assertNull(actualMaxPrice);
    }

    @Test
    void testGetMinPriceWithOffers() {
        BigDecimal minPrice = new BigDecimal("100.00");

        Offer offer = new Offer();
        offer.setPrice(minPrice);

        when(offerRepository.findTopByOrderByPrice()).thenReturn(offer);

        BigDecimal actualMinPrice = offerService.getMinPrice();

        assertEquals(minPrice, actualMinPrice);
    }

    @Test
    void testGetMinPriceWithNullPrice() {
        Offer offer = new Offer();
        offer.setPrice(null);

        when(offerRepository.findTopByOrderByPrice()).thenReturn(offer);

        BigDecimal actualMinPrice = offerService.getMinPrice();

        assertNull(actualMinPrice);
    }

    @Test
    void testCreateOffer() {
        User manager = new User();
        manager.setEmail(MANAGER_EMAIL);

        InsuranceType insuranceType = new InsuranceType();

        OfferDto offerDto = new OfferDto();

        Offer savedOffer = new Offer();
        savedOffer.setId(OFFER_ID);

        when(userService.getByEmail(MANAGER_EMAIL)).thenReturn(manager);
        when(companyService.getCompanyByManager(manager)).thenReturn(new Company());
        when(offerMapper.toOffer(offerDto)).thenReturn(new Offer());
        when(offerRepository.save(any(Offer.class))).thenReturn(savedOffer);

        Long actualOfferId = offerService.createOffer(offerDto, insuranceType, MANAGER_EMAIL);

        assertEquals(OFFER_ID, actualOfferId);
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void testDeleteOfferSuccessfully() {
        User manager = new User();
        Offer offer = new Offer();
        offer.setId(OFFER_ID);

        when(userService.getByEmail(MANAGER_EMAIL)).thenReturn(manager);
        when(offerRepository.findOne(any(OfferFilter.class))).thenReturn(Optional.of(offer));
        when(offerRepository.save(offer)).thenReturn(offer);

        assertDoesNotThrow(() -> offerService.deleteOffer(MANAGER_EMAIL, OFFER_ID));

        assertFalse(offer.getActive());
        verify(userService, times(1)).getByEmail(MANAGER_EMAIL);
        verify(offerRepository, times(1)).findOne(any(OfferFilter.class));
        verify(offerRepository, times(1)).save(offer);
    }


    @Test
    void testDeleteOfferWithNotFoundOffer() {
        User manager = new User();

        when(userService.getByEmail(MANAGER_EMAIL)).thenReturn(manager);
        when(offerRepository.findOne(any(OfferFilter.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offerService.deleteOffer(MANAGER_EMAIL, OFFER_ID));

        verify(userService, times(1)).getByEmail(MANAGER_EMAIL);
        verify(offerRepository, times(1)).findOne(any(OfferFilter.class));
        verify(offerRepository, times(0)).save(any(Offer.class));
    }
    
}
