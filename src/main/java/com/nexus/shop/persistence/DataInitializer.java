package com.nexus.shop.persistence;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.persistence.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository repository;

    @Override
    public void run(String... args) {
        DataInitializer.log.info("Verificando banco de dados");
        if (repository.count() == 0) {
            DataInitializer.log.info("Nenhum dado encontrado, inicializando banco de dados com produtos");

            repository.saveAll(List.of(
                    new Product("Smartphone X", "High-end smartphone", new BigDecimal("2999.99"), 50,
                            Category.ELETRONICOS),
                    new Product("Laptop Pro", "Powerful laptop", new BigDecimal("5999.90"), 20, Category.ELETRONICOS),
                    new Product("Wireless Headphones", "Noise-cancelling over-ear headphones", new BigDecimal("899.99"),
                            35, Category.ELETRONICOS),
                    new Product("Office Chair", "Ergonomic chair for office use", new BigDecimal("649.50"), 15,
                            Category.MOVEIS),
                    new Product("Gaming Mouse", "High precision RGB gaming mouse", new BigDecimal("199.90"), 80,
                            Category.ELETRONICOS),
                    new Product("Mechanical Keyboard", "RGB mechanical keyboard with blue switches",
                            new BigDecimal("349.90"), 60, Category.ELETRONICOS),
                    new Product("Running Shoes", "Comfortable running shoes", new BigDecimal("249.99"), 40,
                            Category.ROUPAS),
                    new Product("Backpack", "Durable backpack for travel and work", new BigDecimal("159.90"), 70,
                            Category.ACESSORIOS),
                    new Product("Coffee Maker", "Automatic drip coffee maker", new BigDecimal("299.00"), 25,
                            Category.ELETRODOMESTICOS),
                    new Product("Desk Lamp", "LED desk lamp with adjustable brightness", new BigDecimal("89.90"), 55,
                            Category.MOVEIS),
                    new Product("Tablet Plus", "10-inch tablet with high resolution screen", new BigDecimal("1899.99"),
                            30, Category.ELETRONICOS),
                    new Product("Smartwatch Fit", "Fitness smartwatch with heart rate monitor",
                            new BigDecimal("799.90"), 45, Category.ELETRONICOS),
                    new Product("Bluetooth Speaker", "Portable speaker with deep bass", new BigDecimal("299.99"), 65,
                            Category.ELETRONICOS),
                    new Product("4K Monitor", "27-inch 4K UHD monitor", new BigDecimal("1799.00"), 18,
                            Category.ELETRONICOS),
                    new Product("USB-C Hub", "Multiport adapter with HDMI and USB 3.0", new BigDecimal("149.90"), 90,
                            Category.ACESSORIOS),
                    new Product("External SSD 1TB", "High-speed portable SSD storage", new BigDecimal("699.90"), 25,
                            Category.ELETRONICOS),
                    new Product("Office Desk", "Spacious wooden office desk", new BigDecimal("899.00"), 12,
                            Category.MOVEIS),
                    new Product("Bookshelf", "5-tier wooden bookshelf", new BigDecimal("499.90"), 20, Category.MOVEIS),
                    new Product("Floor Lamp", "Modern floor lamp with warm lighting", new BigDecimal("229.90"), 28,
                            Category.MOVEIS),
                    new Product("Air Fryer", "Oil-free air fryer with digital panel", new BigDecimal("399.99"), 33,
                            Category.ELETRODOMESTICOS),
                    new Product("Blender Pro", "High-power blender with glass jar", new BigDecimal("249.90"), 40,
                            Category.ELETRODOMESTICOS),
                    new Product("Vacuum Cleaner", "Bagless vacuum cleaner with strong suction",
                            new BigDecimal("599.00"), 22, Category.ELETRODOMESTICOS),
                    new Product("T-shirt Basic", "Cotton basic t-shirt", new BigDecimal("49.90"), 100, Category.ROUPAS),
                    new Product("Jeans Slim Fit", "Comfortable slim fit jeans", new BigDecimal("129.90"), 55,
                            Category.ROUPAS),
                    new Product("Jacket Winter", "Warm winter jacket", new BigDecimal("299.90"), 35, Category.ROUPAS),
                    new Product("Sneakers Casual", "Stylish casual sneakers", new BigDecimal("199.90"), 60,
                            Category.ROUPAS),
                    new Product("Sunglasses", "UV protection sunglasses", new BigDecimal("89.90"), 75,
                            Category.ACESSORIOS),
                    new Product("Wallet Leather", "Genuine leather wallet", new BigDecimal("119.90"), 50,
                            Category.ACESSORIOS),
                    new Product("Travel Suitcase", "Medium size durable suitcase", new BigDecimal("349.90"), 27,
                            Category.ACESSORIOS),
                    new Product("Electric Kettle", "Fast boiling electric kettle", new BigDecimal("159.90"), 38,
                            Category.ELETRODOMESTICOS),
                    new Product("Gaming Chair", "Ergonomic gaming chair with lumbar support", new BigDecimal("1299.90"),
                            14, Category.MOVEIS),
                    new Product("Router Wi-Fi 6", "High-speed dual band Wi-Fi 6 router", new BigDecimal("499.90"), 32,
                            Category.ELETRONICOS),
                    new Product("Webcam HD", "1080p webcam with built-in microphone", new BigDecimal("219.90"), 46,
                            Category.ELETRONICOS),
                    new Product("Microphone USB", "Studio quality USB microphone", new BigDecimal("349.90"), 29,
                            Category.ELETRONICOS),
                    new Product("Power Bank 20000mAh", "Portable fast-charging power bank", new BigDecimal("179.90"),
                            70, Category.ACESSORIOS),
                    new Product("Smart TV 50\"", "50-inch 4K Smart TV with HDR", new BigDecimal("2799.00"), 10,
                            Category.ELETRONICOS),
                    new Product("Soundbar", "Home theater soundbar system", new BigDecimal("899.90"), 21,
                            Category.ELETRONICOS),
                    new Product("Hair Dryer", "Compact hair dryer with multiple settings", new BigDecimal("129.90"), 58,
                            Category.ELETRODOMESTICOS),
                    new Product("Electric Iron", "Steam electric iron", new BigDecimal("149.90"), 44,
                            Category.ELETRODOMESTICOS),
                    new Product("Mixer", "Kitchen mixer with multiple speeds", new BigDecimal("299.90"), 26,
                            Category.ELETRODOMESTICOS),
                    new Product("Dress Casual", "Light casual dress for everyday wear", new BigDecimal("139.90"), 48,
                            Category.ROUPAS),
                    new Product("Shorts Sport", "Breathable sports shorts", new BigDecimal("79.90"), 67,
                            Category.ROUPAS),
                    new Product("Cap Adjustable", "Adjustable cap with curved brim", new BigDecimal("59.90"), 85,
                            Category.ACESSORIOS),
                    new Product("Notebook 15\"", "15-inch notebook for daily tasks", new BigDecimal("3499.00"), 16,
                            Category.ELETRONICOS),
                    new Product("Mouse Pad XL", "Extended mouse pad for desk setup", new BigDecimal("89.90"), 73,
                            Category.ACESSORIOS),
                    new Product("LED Strip Lights", "RGB LED strip with remote control", new BigDecimal("119.90"), 52,
                            Category.ACESSORIOS),
                    new Product("Bedside Table", "Compact bedside table with drawer", new BigDecimal("279.90"), 19,
                            Category.MOVEIS),
                    new Product("Wardrobe 3 Doors", "Spacious wardrobe with 3 doors", new BigDecimal("1599.00"), 8,
                            Category.MOVEIS),
                    new Product("Dish Rack", "Stainless steel dish drying rack", new BigDecimal("99.90"), 61,
                            Category.ELETRODOMESTICOS),
                    new Product("Toaster", "2-slice toaster with browning control", new BigDecimal("139.90"), 37,
                            Category.ELETRODOMESTICOS)
            ));
        } else {
            DataInitializer.log.info("Dados já existentes, pulando inicialização do banco de dados");
        }
    }

}
