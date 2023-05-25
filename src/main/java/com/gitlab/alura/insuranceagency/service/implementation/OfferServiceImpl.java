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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private static final Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);

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
    public Page<OfferDto> getAll(Pageable pageable, OfferFilter offerFilter) {
        Page<Offer> offerPage = offerRepository.findAll(offerFilter, pageable);
        List<OfferDto> offerDtoList = offerPage
                .getContent()
                .stream()
                .map(offerMapper::toOfferDto)
                .collect(Collectors.toList());
        return new PageImpl<>(offerDtoList, pageable, offerPage.getTotalElements());
    }

    @Override
    public Page<OfferDto> getAll(Pageable pageable, String userEmail) {
        User user = userService.getByEmail(userEmail);
        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setActive(true);
        offerFilter.setUser(user);
        return getAll(pageable, offerFilter);
    }

    @Override
    public OfferDto getDtoById(Long id) {
        Offer offer = getById(id);
        return offerMapper.toOfferDto(offer);
    }

    @Override
    public OfferDto getOfferByManagerAndId(String userEmail, Long id) {
        User user = userService.getByEmail(userEmail);
        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setActive(true);
        offerFilter.setUser(user);
        offerFilter.setId(id);
        Offer offer = offerRepository.findOne(offerFilter).orElseThrow(NotFoundException::new);
        return offerMapper.toOfferDto(offer);
    }

    @Override
    public Offer getById(Long id) {
        return offerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public BigDecimal getMaxPrice(){
        return offerRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public BigDecimal getMinPrice(){
        return offerRepository.findTopByOrderByPrice().getPrice();
    }

    @Override
    public Long createOffer(OfferDto offerDto,
                            InsuranceType insuranceType,
                            String managerEmail) {
        logger.info("Creating a new offer");
        User manager = userService.getByEmail(managerEmail);
        Company company = companyService.getCompanyByManager(manager);

        Offer offer = offerMapper.toOffer(offerDto);
        offer.setId(null);
        offer.setActive(true);
        offer.setInsuranceType(insuranceType);
        offer.setCompany(company);

        offer = offerRepository.save(offer);
        logger.info("New offer created with ID: {}", offer.getId());
        return offer.getId();
    }

    @Override
    public Long updateOffer(OfferDto offerDto, InsuranceType insuranceType, String managerEmail) {
        deleteOffer(managerEmail, offerDto.getId());
        return createOffer(offerDto, insuranceType, managerEmail);
    }

    @Override
    public void deleteOffer(String managerEmail, Long offerId) {
        logger.info("Deleting offer with ID: {}", offerId);
        User manager = userService.getByEmail(managerEmail);

        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setId(offerId);
        offerFilter.setUser(manager);

        Offer offer = offerRepository.findOne(offerFilter).orElseThrow(NotFoundException::new);
        offer.setActive(false);
        offerRepository.save(offer);
        logger.info("Offer with ID: {} deleted successfully", offerId);
    }
}
