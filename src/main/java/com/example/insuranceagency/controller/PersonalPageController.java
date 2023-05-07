package com.example.insuranceagency.controller;


import com.example.insuranceagency.entity.User;
import com.example.insuranceagency.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RequestMapping("/personal")
public class PersonalPageController {

    private final UserService userService;

    public PersonalPageController(UserService userService) {
        this.userService = userService;
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
        return "personal/info";
    }
}
