package com.gitlab.alura.insuranceagency.controller;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.DocumentType;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
import com.gitlab.alura.insuranceagency.service.InsuranceTypeService;
import com.gitlab.alura.insuranceagency.service.OfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/personal/offers")
public class CompanyOfferController {
    private final OfferService offerService;
    private final InsuranceTypeService insuranceTypeService;

    private final DocumentTypeService documentTypeService;

    public CompanyOfferController(OfferService offerService,
                                  InsuranceTypeService insuranceTypeService,
                                  DocumentTypeService documentTypeService) {
        this.offerService = offerService;
        this.insuranceTypeService = insuranceTypeService;
        this.documentTypeService = documentTypeService;
    }

    @GetMapping("")
    public String getOffers(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "10") int size,
                            Principal principal,
                            Model model){
        String userEmail = principal.getName();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OfferDto> offerDtoPage = offerService.getAll(pageable, userEmail);

        model.addAttribute("offers", offerDtoPage);
        model.addAttribute("page", "offers");
        return "personal/personal-account";
    }

    @GetMapping("/{id}")
    public String getOfferById(@PathVariable(name = "id") Long id,
                               Principal principal,
                               Model model){
        String userEmail = principal.getName();
        OfferDto offerDto = offerService.getOfferByManagerAndId(userEmail, id);

        model.addAttribute("offer", offerDto);
        model.addAttribute("page", "offers");
        return "personal/offer";
    }

    @GetMapping("/new")
    public String getNewOfferForm(Model model){
        List<InsuranceTypeDto> typeDtoList = insuranceTypeService.getInsuranceTypes();
        List<DocumentType> documentTypes = documentTypeService.getAllActive();

        model.addAttribute("offer", new OfferDto());
        model.addAttribute("types", typeDtoList);
        model.addAttribute("documentTypes", documentTypes);
        model.addAttribute("page", "offers");
        return "personal/offer";
    }

    @PostMapping("/new")
    public String createOffer(@ModelAttribute("offer") OfferDto offerDto,
                              @RequestParam(name = "type", required = false, defaultValue = "1") Long typeId,
                              Principal principal){
        String userEmail = principal.getName();
        InsuranceType insuranceType = insuranceTypeService.getById(typeId);
        Long offerId = offerService.createOffer(offerDto, insuranceType, userEmail);
        return "redirect:/personal/offers/" + offerId;
    }

    @DeleteMapping("/{id}")
    public String deleteOffer(@PathVariable("id") Long id,
                                              Principal principal) {
        String managerEmail = principal.getName();
        offerService.deleteOffer(managerEmail, id);
        return "redirect:/personal/offers";
    }
    @GetMapping("/{id}/getOfferForm")
    public String getOfferForm(@PathVariable("id") Long id,
                               Principal principal,
                               Model model) {
        String managerEmail = principal.getName();
        OfferDto offerDto = offerService.getOfferByManagerAndId(managerEmail, id);
        List<InsuranceTypeDto> typeDtoList = insuranceTypeService.getInsuranceTypes();
        List<DocumentType> documentTypes = documentTypeService.getAllActive();


        model.addAttribute("offer", offerDto);
        model.addAttribute("page", "offers");
        model.addAttribute("edit", true);
        model.addAttribute("types", typeDtoList);
        model.addAttribute("documentTypes", documentTypes);

        return "/personal/offer";
    }

    @PutMapping("/{id}")
    public String updateOfferById(@PathVariable(name = "id") Long id,
                                  @ModelAttribute("offer") OfferDto offerDto,
                                  @RequestParam(name = "type", required = false, defaultValue = "1") Long typeId,
                                  Principal principal){
        String userEmail = principal.getName();
        InsuranceType insuranceType = insuranceTypeService.getById(typeId);
        Long offerId = offerService.updateOffer(offerDto, insuranceType, userEmail);
        return "redirect:/personal/offers/" + offerId;
    }
}
