package com.example.demae.controller;

import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:8080",allowedHeaders = "*")
@RequestMapping("/api/lambda")
public class ConnectTestController {

        @GetMapping("/{orderId}")
        public void in(@PathVariable Long orderId) {
            System.out.println(orderId + "상태변경 완료");
        }

}
