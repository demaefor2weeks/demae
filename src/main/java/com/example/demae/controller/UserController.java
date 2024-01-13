package com.example.demae.controller;


import com.example.demae.dto.login.SignupRequestDto;
import com.example.demae.service.UserService;
import lombok.AllArgsConstructor;
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
    private final UserService userService;

    @GetMapping
    public String signUpAdrees(){
        return "singUp";
    }

    @PostMapping
    public String signUpAdrees1(@ModelAttribute SignupRequestDto signupRequestDto, Model model){
        System.out.println(signupRequestDto.getName());
        userService.signup(signupRequestDto);
        return "users";
    }
    @GetMapping("/asdf")
    public String AA(){
        return "asdf";
    }
}
