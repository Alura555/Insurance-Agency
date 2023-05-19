package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.dto.PolicyDto;
import com.gitlab.alura.insuranceagency.entity.Offer;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface OfferService {
    Page<OfferDto> findAll(Pageable pageable, OfferFilter offerFilter);
    Page<OfferDto> findAll(Pageable pageable, String userEmail);

    OfferDto findDtoById(Long id);

    OfferDto getOfferByUserAndId(String userEmail, Long id);


    Offer findById(Long id);

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();
}
