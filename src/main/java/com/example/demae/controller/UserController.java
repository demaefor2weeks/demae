package com.example.demae.controller;


import com.example.demae.dto.login.SignupRequestDto;
import com.example.demae.entity.User;
import com.example.demae.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
//
    private UserService userService;

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
    @GetMapping("/loginForm")
    public String AA(){
        return "login";
    }

    @GetMapping("/main")
    public String main(){
        return "main";
    }
}
