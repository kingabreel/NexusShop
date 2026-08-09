package com.nexus.shop.api.promotion.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.promotion.service.PromotionService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.promotion.dto.PromotionPatchDTO;
import com.nexus.shop.model.promotion.request.PromotionRequestDTO;
import com.nexus.shop.model.promotion.response.PromotionResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PromotionResponseDTO>> create(@RequestBody @Valid PromotionRequestDTO dto) {
        try {
            final PromotionResponseDTO response = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Successfully created promotion"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while creating promotion"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PromotionResponseDTO>>> getAll() {
        try {
            final List<PromotionResponseDTO> response = service.getAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully listed promotions"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while listing promotions"));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PromotionResponseDTO>>> getActive() {
        try {
            final List<PromotionResponseDTO> response = service.getActive();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully listed active promotions"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while listing active promotions"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PromotionResponseDTO>> getById(@PathVariable UUID id) {
        try {
            final PromotionResponseDTO response = service.getById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully retrieved promotion"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while retrieving promotion"));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PromotionResponseDTO>> update(
            @PathVariable UUID id,
            @RequestBody @Valid PromotionPatchDTO dto) {
        try {
            final PromotionResponseDTO response = service.update(id, dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successful promotion update"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error updating promotion"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        try {
            service.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(null, "Promotion deletion successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error deleting promotion"));
        }
    }

    @PostMapping("/{id}/products/{productId}")
    public ResponseEntity<ApiResponse<PromotionResponseDTO>> addPromotionProduct(
            @PathVariable UUID id,
            @PathVariable UUID productId) {
        try {
            final PromotionResponseDTO response = service.addPromotionProduct(id, productId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Product successfully added to promotion"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error adding product to promotion"));
        }
    }

    @DeleteMapping("/{id}/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> deletePromotionProduct(
            @PathVariable UUID id,
            @PathVariable UUID productId) {
        try {
            service.deletePromotionProduct(productId, id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(null, "Product successfully removed from promotion"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error removing product from promotion"));
        }
    }
}
