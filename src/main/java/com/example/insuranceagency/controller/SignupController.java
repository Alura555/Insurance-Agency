package com.example.insuranceagency.controller;

import com.example.insuranceagency.dto.UserDto;
import com.example.insuranceagency.exception.InvalidInputException;
import com.example.insuranceagency.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("userDto") UserDto userDto,
                         BindingResult result, Model model) {
        try {
            userService.registerNewUser(userDto, "CLIENT");
        } catch (InvalidInputException e){
            result.rejectValue(e.getField(), "error.userDto", e.getMessage());
            return "signup";
        }
        return "redirect:/login?registerSuccess";
    }
}
