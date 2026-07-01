package com.nexus.shop.api.store.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.shop.api.store.service.StoreService;
import com.nexus.shop.model.ApiResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/store")
@RestController
public class StoreController {
    
    private final StoreService service;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody @Valid Object dto) {

        try {
            final Object response = service.create(dto);
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

    @PostMapping("/revoke/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable final UUID id) {

        try {
            service.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>(null, "Successfully deleted store"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while deleting store"));
        }
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResponse<Object>> findById(@PathVariable final UUID id) {

        try {
            final Object response = service.findById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully found store"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while finding store"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> findAll() {

        try {
            final Object response = service.getAll();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(response, "Successfully found all stores"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Internal error while finding all stores"));
        }
    }


    // TODO: Ainda em planejamento

    // @GetMapping("/metrics/{id}") 
    // public ResponseEntity<ApiResponse<Object>> getMetrics(@PathVariable final String id) {

    //     try {

    //         return ResponseEntity.status(HttpStatus.OK)
    //                 .body(new ApiResponse<>(response, "Successfully created rating"));
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity
    //                 .status(HttpStatus.BAD_REQUEST)
    //                 .body(new ApiResponse<>(null, e.getMessage()));

    //     } catch (Exception e) {
    //         return ResponseEntity
    //                 .status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(new ApiResponse<>(null, "Internal error while creating rating"));
    //     }
    // }

}
