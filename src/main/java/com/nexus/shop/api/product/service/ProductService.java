package com.nexus.shop.api.product.service;

import com.nexus.shop.model.product.dto.ProductCreateDTO;
import com.nexus.shop.model.product.dto.ProductResponseDTO;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.persistence.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponseDTO create(ProductCreateDTO dto) {
        Product product = new Product(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getStock(),
                dto.getCategory()
        );

        Product saved = repository.save(product);

        return toDTO(saved);
    }

    public List<ProductResponseDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ProductResponseDTO findById(Long id){
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return toDTO(product);
    }

    public ProductResponseDTO update(Long id, ProductUpdateDTO dto){

        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setStock(dto.stock());
        existing.setCategory(dto.category());

        Product updated = repository.save(existing);

        return toDTO(updated);
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

        return toDTO(updated);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    private ProductResponseDTO toDTO(Product product) {
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