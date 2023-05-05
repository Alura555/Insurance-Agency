package com.example.insuranceagency.service;

import com.example.insuranceagency.dto.OfferDto;
import com.example.insuranceagency.filter.OfferFilter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface OfferService {
    Page<OfferDto> findAll(int page, int size, OfferFilter offerFilter, int sort);

    OfferDto findById(Long id);

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();

    List<String> getSortTypes();
}
