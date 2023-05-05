package com.example.insuranceagency.service.implementation;

import com.example.insuranceagency.dto.OfferDto;
import com.example.insuranceagency.entity.Offer;
import com.example.insuranceagency.filter.OfferFilter;
import com.example.insuranceagency.mapper.OfferMapper;
import com.example.insuranceagency.repository.OfferRepository;
import com.example.insuranceagency.service.OfferService;
import com.example.insuranceagency.sorting.SortOption;
import com.example.insuranceagency.sorting.SortOptionsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final SortOptionsList sortOptions;

    public OfferServiceImpl(OfferRepository offerRepository, SortOptionsList sortOptions) {
        this.offerRepository = offerRepository;
        this.sortOptions = sortOptions;
    }

    public List<String> getSortTypes(){
        return sortOptions.getSortOptionNames();
    }

    @Override
    public Page<Offer> findAll(int page, int size, OfferFilter offerFilter, int sortType) {
        Sort sortOption = sortOptions.getSortByIndex(sortType);
        Pageable pageable = PageRequest.of(page, size, sortOption);
        return offerRepository.findAll(offerFilter, pageable);
    }

    public Offer findById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    public BigDecimal getMaxPrice(){
        return offerRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public BigDecimal getMinPrice(){
        return offerRepository.findTopByOrderByPrice().getPrice();
    }
}
