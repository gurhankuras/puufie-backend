package com.kuras.learnspring.learnspring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/public/ping")
    public String ping() { return "pong"; }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, authenticated user!";
    }
}