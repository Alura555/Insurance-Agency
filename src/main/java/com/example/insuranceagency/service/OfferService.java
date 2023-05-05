package com.example.insuranceagency.service;

import com.example.insuranceagency.entity.Offer;
import com.example.insuranceagency.filter.OfferFilter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface OfferService {
    Page<Offer> findAll(int page, int size, OfferFilter offerFilter, int sort);

    Offer findById(Long id);

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();

    List<String> getSortTypes();
}
