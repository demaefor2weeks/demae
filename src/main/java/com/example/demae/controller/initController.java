package com.example.demae.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class initController {
    @GetMapping("/")
    public String home(){return "store";}

}
