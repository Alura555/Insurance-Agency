package com.example.insuranceagency.controller;

import com.example.insuranceagency.dto.InsuranceTypeDto;
import com.example.insuranceagency.dto.OfferDto;
import com.example.insuranceagency.entity.Company;
import com.example.insuranceagency.entity.Offer;
import com.example.insuranceagency.exception.NotFoundException;
import com.example.insuranceagency.filter.OfferFilter;
import com.example.insuranceagency.mapper.OfferMapper;
import com.example.insuranceagency.service.CompanyService;
import com.example.insuranceagency.service.InsuranceTypeService;
import com.example.insuranceagency.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offers")
public class OffersController {
    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private InsuranceTypeService insuranceTypeService;


    @GetMapping("")
    public String getOffers(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "12") int size,
                            @RequestParam(name = "q", required = false) String query,
                            @RequestParam(name = "sort", required = false, defaultValue = "1") Integer sort,
                            @RequestParam(name = "type", required = false, defaultValue = "0") Long type,
                            @RequestParam(name = "company", required = false, defaultValue = "0") Long company,
                            @RequestParam(name = "minPrice", required = false) Integer selectedMinPrice,
                            @RequestParam(name = "maxPrice", required = false) Integer selectedMaxPrice,
                            Model model){
        if (page < 0 || size < 0){
            throw new NotFoundException();
        }
        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setActive(true);
        offerFilter.setInsuranceType(type);
        offerFilter.setMinPrice(selectedMinPrice);
        offerFilter.setMaxPrice(selectedMaxPrice);
        offerFilter.setCompany(company);
        offerFilter.setSearchQuery(query);



        Page<Offer> offers = offerService.findAll(page, size, offerFilter, sort - 1);
        List<OfferDto> offerDtos = offers.get()
                .map(x -> offerMapper.toOfferDto(x))
                .collect(Collectors.toList());

        List<InsuranceTypeDto> insuranceTypeList = insuranceTypeService.getInsuranceTypes();
        List<Company> companies = companyService.getActiveCompanies();
        List<String> sortOptions = offerService.getSortTypes();
        BigDecimal maxPrice = offerService.getMaxPrice();
        BigDecimal minPrice = offerService.getMinPrice();


        model.addAttribute("offers", offerDtos);
        model.addAttribute("types", insuranceTypeList);
        model.addAttribute("companies", companies);
        model.addAttribute("totalPages", offers.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedMaxPrice", selectedMaxPrice);
        model.addAttribute("selectedMinPrice", selectedMinPrice);
        model.addAttribute("selectedCompany", company);
        model.addAttribute("query", query);
        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minPrice", minPrice);
        return "offers/offers";
    }

    @GetMapping("/{id}")
    public String getOfferById(Model model, @PathVariable Long id){
        Offer offer = offerService.findById(id);
        if (offer == null){
            throw new NotFoundException();
        }
        OfferDto offerDto = offerMapper.toOfferDto(offer);
        model.addAttribute("offer", offerDto);
        return "offers/page";
    }

    public OffersController(OfferService offerService,
                            OfferMapper offerMapper,
                            CompanyService companyService,
                            InsuranceTypeService insuranceTypeService) {
        this.offerService = offerService;
        this.offerMapper = offerMapper;
        this.companyService = companyService;
        this.insuranceTypeService = insuranceTypeService;
    }
}
