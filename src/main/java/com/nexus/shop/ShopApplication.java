package com.nexus.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {
	
	private ShopApplication() {
        throw new UnsupportedOperationException("Utility class");
    }

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
