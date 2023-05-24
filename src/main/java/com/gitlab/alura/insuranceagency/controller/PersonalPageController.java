package com.gitlab.alura.insuranceagency.controller;


import com.gitlab.alura.insuranceagency.dto.PolicyDto;
import com.gitlab.alura.insuranceagency.entity.User;
import com.gitlab.alura.insuranceagency.exception.InvalidInputException;
import com.gitlab.alura.insuranceagency.service.PolicyService;
import com.gitlab.alura.insuranceagency.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;


@Controller
@RequestMapping("/personal")
public class PersonalPageController {

    private final UserService userService;
    private final PolicyService policyService;

    public PersonalPageController(UserService userService, PolicyService policyService) {
        this.userService = userService;
        this.policyService = policyService;
    }


    @GetMapping
    public RedirectView redirectToHomePage() {
        return new RedirectView("/personal/info");
    }

    @GetMapping("/info")
    public String getInfo(Model model, Principal principal){
        String email = principal.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("page", "info");
        return "personal/personal-account";
    }

    @GetMapping("/policies")
    public String getPoliciesOrApplications(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            Model model, Principal principal) {

        String email = principal.getName();
        Sort sort = Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PolicyDto> policies = policyService.getPoliciesByUser(email, pageable);
        model.addAttribute("page", "policies");
        model.addAttribute("policies", policies);
        return "personal/personal-account";
    }

    @GetMapping("/applications")
    public String getApplications(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                  Model model, Principal principal) {
        String email = principal.getName();
        Sort sort = Sort.by("creationDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PolicyDto> policies = policyService.getApplicationsByUser(email, pageable);
        model.addAttribute("page", "applications");
        model.addAttribute("policies", policies);
        return "personal/personal-account";
    }

    @GetMapping("/policies/{id}")
    public String getPolicyById(@PathVariable(name = "id") Long id,
                                            Model model, Principal principal) {
        String email = principal.getName();
        PolicyDto policy = policyService.getPolicyByUserAndId(email, id);
        model.addAttribute("page", "policies");
        model.addAttribute("policyDto", policy);
        return "personal/policy";
    }

    @GetMapping("/applications/{id}")
    public String getApplicationById(@PathVariable(name = "id") Long id,
                                     Model model, Principal principal) {
        String email = principal.getName();
        PolicyDto policy = policyService.getApplicationByUserAndId(email, id);
        model.addAttribute("page", "applications");
        model.addAttribute("policyDto", policy);
        return "personal/policy";
    }

    @PostMapping("/applications/{id}")
    public String updatePolicy(@PathVariable("id") Long id,
                               @ModelAttribute("policyDto") PolicyDto policyDto,
                               BindingResult result) {
        try {
            policyService.updatePolicy(id, policyDto);
        } catch (InvalidInputException e) {
            result.rejectValue(e.getField(), "error.policyDto", e.getMessage());
            return "personal/policy";
        }
        return "redirect:/personal/applications/" + id;
    }

    @PutMapping("/applications/{id}")
    public String handleApplication(@PathVariable("id") Long id,
                                    @RequestParam("action") String action,
                                    Principal principal) {
        String email = principal.getName();
        policyService.handleApplication(email, action, id);
        return "redirect:/personal/policies/" + id;
    }
    @GetMapping("/applications/new")
    public String getNewApplicationForm(@RequestParam("id") Long id,
                                        Principal principal,
                                        Model model) {
        String email = principal.getName();
        PolicyDto applicationDto = policyService.getApplicationForm(email, id);
        model.addAttribute("policyDto", applicationDto);
        model.addAttribute("offerId", id);

        return "personal/policy";
    }
    @PostMapping("/applications/new")
    public String createNewApplication(@RequestParam("id") Long offerId,
                                       Principal principal,
                                       PolicyDto policyDto,
                                       Model model,
                                       BindingResult result) {
        String email = principal.getName();
        Long id = null;
        try {
            id = policyService.createApplication(email, policyDto, offerId);
        } catch (InvalidInputException e) {
            result.rejectValue(e.getField(), "error.policyDto", e.getMessage());
            return "personal/policy";
        }
        return "redirect:/personal/applications/" + id;
    }

}
