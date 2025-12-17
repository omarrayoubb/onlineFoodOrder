package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 1. Tells Spring: "This class handles web requests"
public class HelloController {

    @GetMapping("/hello") // 2. Tells Spring: "When user visits /hello, run this"
    public String sayHello() {
        // 3. The return value is sent back to the browser
        return "Hello! I am your first Spring Boot Controller.";
    }
}