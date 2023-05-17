package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface OfferService {
    Page<OfferDto> findAll(Pageable pageable, OfferFilter offerFilter);

    OfferDto findById(Long id);

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();
}
