package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import com.gitlab.alura.insuranceagency.entity.Offer;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface OfferService {
    Page<OfferDto> getAll(Pageable pageable, OfferFilter offerFilter);
    Page<OfferDto> getAll(Pageable pageable, String userEmail);

    OfferDto getDtoById(Long id);

    OfferDto getOfferByManagerAndId(String userEmail, Long id);


    Offer getById(Long id);

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();

    Long createOffer(OfferDto offerDto, InsuranceType insuranceType, String managerEmail);
    Long updateOffer(OfferDto offerDto, InsuranceType insuranceType, String managerEmail);

    void deleteOffer(String managerEmail, Long offerId);
}
