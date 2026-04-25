package com.nexus.shop.api.product.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.nexus.shop.api.product.service.ProductService;
import com.nexus.shop.model.product.dto.ProductCreateDTO;
import com.nexus.shop.model.product.dto.ProductResponseDTO;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponseDTO create(@RequestBody @Valid ProductCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ProductResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateDTO dto) {

        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public ProductResponseDTO partialUpdate(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO dto) {

        return service.updatePartial(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}