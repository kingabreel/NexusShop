package com.nexus.shop.api.product.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import com.nexus.shop.api.product.service.ProductService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.product.dto.ProductPatchDTO;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;
import com.nexus.shop.model.product.enums.Category;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> create(@RequestBody @Valid ProductCreateDTO dto) {

        try {
            final ProductResponseDTO response = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Successfully created product"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while creating product"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "name") String sort) {
        try {
            final Pageable pageable = PageRequest.of(
                    page != null ? page : 0,
                    size != null ? size : 10,
                    Sort.by(sort != null ? sort : "id").ascending());

            final Page<ProductResponseDTO> response = service.findAll(
                    name, minPrice, maxPrice, category, pageable);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Success in listing the products"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while listing products"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> findById(@PathVariable Long id) {
        try {
            final ProductResponseDTO response = service.findById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully searched for product by ID"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while listing product by ID"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateDTO dto) {
        try {
            final ProductResponseDTO response = service.update(id, dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Product update successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error updating product"));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> partialUpdate(
            @PathVariable Long id,
            @RequestBody ProductPatchDTO dto) {
        try {
            final ProductResponseDTO response = service.updatePartial(id, dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successful partial product update"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error updating product partially"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(null, "Product deletion successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error deleting product"));
        }
    }
}
