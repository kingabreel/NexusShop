package com.nexus.shop.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public final class TestController {
    
    @GetMapping
    public String test() {
        return "Hello, NexusShop!";
    }
}
