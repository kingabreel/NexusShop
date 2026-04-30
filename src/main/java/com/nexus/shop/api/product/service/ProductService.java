package com.nexus.shop.api.product.service;

import com.nexus.shop.model.product.dto.ProductUpdateDTO;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;
import com.nexus.shop.utils.converters.ConverterUtil;

import jakarta.persistence.EntityNotFoundException;

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
                dto.category()
        );

        Product saved = repository.save(product);

        return ConverterUtil.toDTO(saved);
    }

    public List<ProductResponseDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(ConverterUtil::toDTO)
                .toList();
    }

    public ProductResponseDTO findById(Long id){
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return ConverterUtil.toDTO(product);
    }

    public ProductResponseDTO update(Long id, ProductUpdateDTO dto){

        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if(dto.name() != null) {
            existing.setName(dto.name());
        }
        existing.setDescription(dto.description());

        if(dto.price() != null) {
            existing.setPrice(dto.price());
        }

        if(dto.stock() != null) {
            existing.setStock(dto.stock());
        }

        if(dto.category() != null){
            existing.setCategory(dto.category());
        }

        Product updated = repository.save(existing);
        return ConverterUtil.toDTO(updated);
    }

    public ProductResponseDTO updatePartial(Long id, ProductUpdateDTO dto){
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException ("Product not found with id: " + id));

        if(dto.name() != null){
            existing.setName(dto.name());
        }
        if(dto.description() != null){
            existing.setDescription(dto.description());
        }
        if(dto.price() != null){
            existing.setPrice(dto.price());
        }
        if(dto.stock() != null){
            existing.setStock(dto.stock());
        }
        if(dto.category() != null){
            existing.setCategory(dto.category());
        }

        Product updated = repository.save(existing);

        return ConverterUtil.toDTO(updated);
    }

    public void delete(final Long id){
        final Product entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        this.repository.delete(entity);
    }

}