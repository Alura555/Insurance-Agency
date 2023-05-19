package com.gitlab.alura.insuranceagency.controller;

import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.repository.OfferRepository;
import com.gitlab.alura.insuranceagency.service.OfferService;
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

import java.security.Principal;

@Controller
@RequestMapping("/personal/offers")
public class CompanyOfferController {
    private final OfferService offerService;

    public CompanyOfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("")
    public String getOffers(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "10") int size,
                            Principal principal,
                            Model model){
        String userEmail = principal.getName();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OfferDto> offerDtoPage = offerService.findAll(pageable, userEmail);

        model.addAttribute("offers", offerDtoPage);
        model.addAttribute("page", "offers");
        return "personal/personal-account";
    }

    @GetMapping("/{id}")
    public String getOfferById(@PathVariable(name = "id") Long id,
                            Principal principal,
                            Model model){
        String userEmail = principal.getName();
        OfferDto offerDto = offerService.getOfferByUserAndId(userEmail, id);

        model.addAttribute("offer", offerDto);
        model.addAttribute("page", "offers");
        return "personal/offer";
    }
}
