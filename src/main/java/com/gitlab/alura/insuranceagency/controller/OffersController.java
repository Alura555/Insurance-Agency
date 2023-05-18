package com.gitlab.alura.insuranceagency.controller;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.Company;
import com.gitlab.alura.insuranceagency.filter.OfferFilter;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import com.gitlab.alura.insuranceagency.service.InsuranceTypeService;
import com.gitlab.alura.insuranceagency.service.OfferService;
import com.gitlab.alura.insuranceagency.sorting.SortOptionsList;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/offers")
public class OffersController {

    private final OfferService offerService;

    private final CompanyService companyService;

    private final InsuranceTypeService insuranceTypeService;

    private final SortOptionsList sortOptionsList;

    public OffersController(OfferService offerService,
                            CompanyService companyService,
                            InsuranceTypeService insuranceTypeService, SortOptionsList sortOptionsList) {
        this.offerService = offerService;
        this.companyService = companyService;
        this.insuranceTypeService = insuranceTypeService;
        this.sortOptionsList = sortOptionsList;
    }

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
        OfferFilter offerFilter = new OfferFilter();
        offerFilter.setActive(true);
        offerFilter.setInsuranceType(type);
        offerFilter.setMinPrice(selectedMinPrice);
        offerFilter.setMaxPrice(selectedMaxPrice);
        offerFilter.setCompany(company);
        offerFilter.setSearchQuery(query);

        if (page < 0 || size < 0){
            page = 0;
            size = 12;
        }
        Sort sortOption = sortOptionsList.getSortByIndex(sort - 1);
        Pageable pageable = PageRequest.of(page, size, sortOption);

        Page<OfferDto> offers = offerService.findAll(pageable, offerFilter);

        List<InsuranceTypeDto> insuranceTypeList = insuranceTypeService.getInsuranceTypes();
        List<Company> companies = companyService.getActiveCompanies();
        List<String> sortOptions = sortOptionsList.getSortOptionNames();
        BigDecimal maxPrice = offerService.getMaxPrice();
        BigDecimal minPrice = offerService.getMinPrice();


        model.addAttribute("offers", offers.getContent());
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
        OfferDto offerDto = offerService.findDtoById(id);
        if (offerDto == null){
            throw new NotFoundException();
        }
        model.addAttribute("offer", offerDto);
        return "offers/page";
    }
}
