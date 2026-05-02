package com.nexus.shop.api.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.nexus.shop.model.product.dto.ProductPatchDTO;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;
import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldCreateProductSuccessfully() {
        ProductCreateDTO dto = new ProductCreateDTO(
                "Phone",
                "Nice phone",
                BigDecimal.valueOf(1000),
                10,
                Category.ELETRONICOS);

        Product saved = new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.category());
        saved.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(saved);

        ProductResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("Phone", result.name());
        assertEquals(BigDecimal.valueOf(1000), result.price());

        verify(repository).save(any(Product.class));
    }

    @Test
    void shouldFindProductById() {
        Product product = new Product(
                "Mouse",
                "Gaming mouse",
                BigDecimal.valueOf(200),
                5,
                Category.ELETRONICOS);
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Mouse", result.name());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.findById(1L));

        assertEquals("Product not found with id: 1", ex.getMessage());
    }

    @Test
    void shouldUpdateProduct() {
        Product existing = new Product(
                "Old",
                "Old desc",
                BigDecimal.valueOf(100),
                1,
                Category.ELETRONICOS);
        existing.setId(1L);

        ProductUpdateDTO dto = new ProductUpdateDTO(
                "New",
                "New desc",
                BigDecimal.valueOf(200),
                2,
                Category.MOVEIS);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenReturn(existing);

        ProductResponseDTO result = service.update(1L, dto);

        assertEquals("New", result.name());
        assertEquals(Category.MOVEIS, result.category());

        verify(repository).save(existing);
    }

    @Test
    void shouldPatchProductPartially() {
        Product existing = new Product(
                "Old",
                "Old desc",
                BigDecimal.valueOf(100),
                1,
                Category.ELETRONICOS);
        existing.setId(1L);

        ProductPatchDTO dto = new ProductPatchDTO(
                null,
                "Updated desc",
                null,
                null,
                null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenReturn(existing);

        ProductResponseDTO result = service.updatePartial(1L, dto);

        assertEquals("Old", result.name());
        assertEquals("Updated desc", result.description());

        verify(repository).save(existing);
    }

    @Test
    void shouldDeleteProduct() {
        Product product = new Product(
                "Test",
                "Desc",
                BigDecimal.TEN,
                1,
                Category.ELETRONICOS);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        verify(repository).delete(product);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnPagedProducts() {
        Product product = new Product(
                "Phone",
                "Desc",
                BigDecimal.TEN,
                1,
                Category.ELETRONICOS);

        Page<Product> page = new PageImpl<>(List.of(product));

        when(repository.findAll(
                any(Specification.class),
                any(Pageable.class))).thenReturn(page);

        Page<ProductResponseDTO> result = service.findAll(
                null, null, null, null, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("Phone", result.getContent().get(0).name());
    }
}