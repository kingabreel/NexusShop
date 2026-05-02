package com.nexus.shop.api.product.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;
import com.nexus.shop.model.product.dto.ProductPatchDTO;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.specification.ProductSpecification;
import com.nexus.shop.utils.converters.ConverterUtil;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponseDTO create(ProductCreateDTO dto) {
        Product product = new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.category());

        Product saved = repository.save(product);

        return ConverterUtil.toDTO(saved);
    }

    public Page<ProductResponseDTO> findAll(
            final String name,
            final BigDecimal minPrice,
            final BigDecimal maxPrice,
            final Category category,
            final Pageable pageable

    ) {
        final Specification<Product> spec = Specification
                .where(ProductSpecification.nameContains(name))
                .and(ProductSpecification.minPrice(minPrice))
                .and(ProductSpecification.maxPrice(maxPrice))
                .and(ProductSpecification.categoryEquals(category));

        return repository.findAll(spec, pageable)
                .map(ConverterUtil::toDTO);
    }

    public ProductResponseDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return ConverterUtil.toDTO(product);
    }

    public ProductResponseDTO update(Long id, ProductUpdateDTO dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setStock(dto.stock());
        existing.setCategory(dto.category());

        Product updated = repository.save(existing);
        return ConverterUtil.toDTO(updated);
    }

    public ProductResponseDTO updatePartial(Long id, ProductPatchDTO dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (dto.name() != null) {
            existing.setName(dto.name());
        }
        if (dto.description() != null) {
            existing.setDescription(dto.description());
        }
        if (dto.price() != null) {
            existing.setPrice(dto.price());
        }
        if (dto.stock() != null) {
            existing.setStock(dto.stock());
        }
        if (dto.category() != null) {
            existing.setCategory(dto.category());
        }

        Product updated = repository.save(existing);

        return ConverterUtil.toDTO(updated);
    }

    public void delete(final Long id) {
        final Product entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        this.repository.delete(entity);
    }

}