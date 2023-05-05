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
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private List<SortOption> sortOptions;

    public List<String> getSortTypes(){
        return sortOptions
                .stream()
                .map(SortOption::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Offer> findAll(int page, int size, OfferFilter offerFilter, int sortType) {
        SortOption sortOption = sortOptions.get(sortType);
        List<Offer> offerList = offerRepository.findAll();
        Pageable pageable = PageRequest.of(page, size, sortOption.getSort());
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
