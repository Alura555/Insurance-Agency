package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.OfferDto;
import com.example.insuranceagency.filter.OfferFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface OfferService {
    Page<OfferDto> findAll(Pageable pageable, OfferFilter offerFilter);

    OfferDto findById(Long id);

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();
}
