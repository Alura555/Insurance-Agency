package com.example.insuranceagency.controller;

import com.example.insuranceagency.dto.InsuranceTypeDto;
import com.example.insuranceagency.entity.Company;
import com.example.insuranceagency.service.CompanyService;
import com.example.insuranceagency.service.InsuranceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    public static final int COMPANY_COUNT = 9;
    public static final int INSURANCE_TYPE_COUNT = 6;
    private final CompanyService companyService;


    private final InsuranceTypeService insuranceTypeService;

    public MainController(CompanyService companyService, InsuranceTypeService insuranceTypeService) {
        this.companyService = companyService;
        this.insuranceTypeService = insuranceTypeService;
    }

    @GetMapping
    public String indexPage(Model model){
        List<Company> companies = companyService.getPopularCompanies(COMPANY_COUNT);
        model.addAttribute("companies", companies);

        List<InsuranceTypeDto> insuranceTypes = insuranceTypeService.getPopularInsuranceTypes(INSURANCE_TYPE_COUNT);
        model.addAttribute("types", insuranceTypes);
        return "index";
    }
}
