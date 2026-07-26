package com.nexus.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class ShopApplication {
	
	private ShopApplication() {

	}

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
