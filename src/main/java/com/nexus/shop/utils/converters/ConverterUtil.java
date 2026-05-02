package com.nexus.shop.utils.converters;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.model.product.entity.Product;

public class ConverterUtil {
    public static ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory()
        );
    }
}
