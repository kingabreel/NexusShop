package com.nexus.shop.api.product.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;
import com.nexus.shop.api.analytics.service.ProductAnalyticService;
import com.nexus.shop.api.analytics.service.UserHistoryService;
import com.nexus.shop.model.product.dto.ProductPatchDTO;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.persistence.specification.ProductSpecification;
import com.nexus.shop.utils.converters.ConverterUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductAnalyticService productAnalyticService;
    private final UserHistoryService userHistoryService;

    @Autowired
    public ProductService(
            final ProductRepository repository,
            final ProductAnalyticService productAnalyticService,
            final UserHistoryService userHistoryService) {
        this.repository = repository;
        this.productAnalyticService = productAnalyticService;
        this.userHistoryService = userHistoryService;
    }

    public ProductResponseDTO create(final ProductCreateDTO dto) {
        Product product = new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.category(),
                new ArrayList<>(),
                false);

        Product saved = this.repository.save(product);

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

        return this.repository.findAll(spec, pageable)
                .map(ConverterUtil::toDTO);
    }

    public ProductResponseDTO findById(final UUID id) {
        final Product product = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (null != this.productAnalyticService && null != this.userHistoryService) {
            try {
                this.productAnalyticService.addProductView(product);
                this.userHistoryService.addProductView(product);
            } catch (Exception e) {
                ProductService.log.error("Error recording product analytic: " + e.getMessage());
            }
        }

        return ConverterUtil.toDTO(product);
    }

    public ProductResponseDTO update(final UUID id, final ProductUpdateDTO dto) {
        final Product existing = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setStock(dto.stock());
        existing.setCategory(dto.category());

        final Product updated = this.repository.save(existing);
        return ConverterUtil.toDTO(updated);
    }

    public ProductResponseDTO updatePartial(final UUID id, final ProductPatchDTO dto) {
        final Product existing = this.repository.findById(id)
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

        final Product updated = this.repository.save(existing);

        return ConverterUtil.toDTO(updated);
    }

    public void delete(final UUID id) {
        final Product entity = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        this.repository.delete(entity);
    }

    public Page<ProductResponseDTO> findHighlighted(final Pageable pageable) {
        final Page<Product> productPage = this.repository.findByHighlight(true, pageable);

        final List<ProductResponseDTO> dtoList = new ArrayList<>();

        for (final Product product : productPage.getContent()) {
            dtoList.add(ConverterUtil.toDTO(product));
        }

        return new PageImpl<>(
                dtoList,
                pageable,
                productPage.getTotalElements());
    }
}
