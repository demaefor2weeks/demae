package com.example.demae.controller;

import com.example.demae.dto.login.SignupRequestDto;
import com.example.demae.entity.User;
import com.example.demae.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "userController")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public String signup(@Validated @ModelAttribute SignupRequestDto requestDto){
        userService.signup(requestDto);
        return "login";
    }

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "main";
    }

}
