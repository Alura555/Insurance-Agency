package com.example.insuranceagency.controller;


import com.example.insuranceagency.dto.PolicyDto;
import com.example.insuranceagency.entity.User;
import com.example.insuranceagency.exception.InvalidInputException;
import com.example.insuranceagency.service.PolicyService;
import com.example.insuranceagency.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

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
        return "personal/personal-page";
    }

    @GetMapping("/{tab:applications|policies}")
    public String getPoliciesOrApplications(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @PathVariable(name = "tab") String tab,
                                            Model model, Principal principal) {
        String email = principal.getName();
        Page<PolicyDto> policies = null;
        if (tab.equals("policies")) {
            policies = policyService.getPoliciesByUser(email, page, size);
        } else if (tab.equals("applications")) {
            policies = policyService.getApplicationsByUser(email, page, size);
        }
        model.addAttribute("page", tab);
        model.addAttribute("policies", policies);
        return "personal/personal-page";
    }
    @GetMapping("/{tab:applications|policies}/{id}")
    public String getPoliciesOrApplicationsById(@PathVariable(name = "id") Long id,
                                            @PathVariable(name = "tab") String tab,
                                            Model model, Principal principal) {
        String email = principal.getName();
        PolicyDto policy = null;
        if (tab.equals("policies")) {
            policy = policyService.getPolicyByUserAndId(email, id);
        } else if (tab.equals("applications")) {
            policy = policyService.getApplicationByUserAndId(email, id);
        }
        model.addAttribute("page", "policy");
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

    @PostMapping("/applications/{id}/approve")
    public String handleApplication(@PathVariable("id") Long id,
                                    @RequestParam("action") String action,
                                    Principal principal) {
        String email = principal.getName();
        policyService.handleApplication(email, action, id);
        return "redirect:/personal/policies/" + id;
    }

}
