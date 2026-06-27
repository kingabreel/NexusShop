package com.nexus.shop.api.rating.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.rating.service.RatingService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.rating.dto.RatingUpdatePartialDTO;
import com.nexus.shop.model.rating.request.RatingCreateDTO;
import com.nexus.shop.model.rating.response.RatingResponseDTO;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RatingResponseDTO>> create(@RequestBody @Valid RatingCreateDTO dto) {

        try {
            final RatingResponseDTO response = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Successfully created rating"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while creating rating"));
        }
    }

    @GetMapping 
    public ResponseEntity<ApiResponse<List<RatingResponseDTO>>> findByIdProduct(@RequestParam final UUID productId) {
        try {
            final List<RatingResponseDTO> response = service.findByProduct(productId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully searched ratings"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while listing ratings"));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RatingResponseDTO>> update(@PathVariable UUID id,
            @RequestBody @Valid RatingUpdatePartialDTO dto) {
        try {
            final RatingResponseDTO response = service.updatePartial(id, dto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successful rating update"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error updating rating partially"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        try {
            service.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(null, "Rating deletion successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Error deleting rating"));
        }
    }

}
