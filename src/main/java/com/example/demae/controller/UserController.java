package com.example.demae.controller;

import com.example.demae.dto.login.SignupRequestDto;
import com.example.demae.entity.User;
import com.example.demae.security.UserDetailsImpl;
import com.example.demae.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    @GetMapping
    public String signUpAdrees(){
        return "signUp";
    }

    @PostMapping
    public String signUpSuccess(@ModelAttribute SignupRequestDto signupRequestDto){
        userService.signup(signupRequestDto);
        return "login";
    }
    @GetMapping("/loginForm")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();

        if (user.getRole().name().equals("ADMIN") && user.getStore() != null) {
            model.addAttribute("storeId", user.getStore().getId());
            return "adminMain";
        }
        if (user.getRole().name().equals("ADMIN") && user.getStore() == null) {
            return "adminMain";
        }
        return "main";
    }

}