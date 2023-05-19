package com.gitlab.alura.insuranceagency.service.implementation;

import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.repository.OfferRepository;
import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.Offer;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import com.gitlab.alura.insuranceagency.mapper.OfferMapper;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import com.gitlab.alura.insuranceagency.service.OfferService;
import com.gitlab.alura.insuranceagency.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;

    private final CompanyService companyService;

    private final UserService userService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            OfferMapper offerMapper,
                            CompanyService companyService,
                            UserService userService) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.companyService = companyService;
        this.userService = userService;
    }

    @Override
    public Page<OfferDto> findAll(Pageable pageable, OfferFilter offerFilter) {
        Page<Offer> offerPage = offerRepository.findAll(offerFilter, pageable);
        List<OfferDto> offerDtoList = offerPage
                .getContent()
                .stream()
                .map(offerMapper::toOfferDto)
                .collect(Collectors.toList());
        return new PageImpl<>(offerDtoList, pageable, offerPage.getTotalElements());
    }

    @Override
    public Page<OfferDto> findAll(Pageable pageable, String userEmail) {
        User user = userService.findByEmail(userEmail);
        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setActive(true);
        offerFilter.setUser(user);
        return findAll(pageable, offerFilter);
    }

    @Override
    public OfferDto findDtoById(Long id) {
        Offer offer = findById(id);
        return offerMapper.toOfferDto(offer);
    }

    @Override
    public OfferDto getOfferByUserAndId(String userEmail, Long id) {
        User user = userService.findByEmail(userEmail);
        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setActive(true);
        offerFilter.setUser(user);
        offerFilter.setId(id);
        Offer offer = offerRepository.findOne(offerFilter).orElseThrow(NotFoundException::new);
        return offerMapper.toOfferDto(offer);
    }

    @Override
    public Offer findById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    public BigDecimal getMaxPrice(){
        return offerRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public BigDecimal getMinPrice(){
        return offerRepository.findTopByOrderByPrice().getPrice();
    }

    @Override
    public Long createNewOffer(OfferDto offerDto,
                               InsuranceType insuranceType,
                               String managerEmail) {
        User manager = userService.findByEmail(managerEmail);
        Company company = companyService.getCompanyByManager(manager);

        Offer offer = offerMapper.toOffer(offerDto);
        offer.setActive(true);
        offer.setInsuranceType(insuranceType);
        offer.setCompany(company);

        return offerRepository.save(offer).getId();
    }
}
