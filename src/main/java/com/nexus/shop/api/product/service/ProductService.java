package com.nexus.shop.api.product.service;

import com.nexus.shop.model.product.dto.ProductUpdateDTO;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.shop.model.product.entity.Product;
import com.nexus.shop.model.product.request.ProductCreateDTO;
import com.nexus.shop.model.product.response.ProductResponseDTO;
import com.nexus.shop.persistence.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponseDTO create(ProductCreateDTO dto) {
        // Modifiquei DTO do formato "class" para "record"
        Product product = new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.category()
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

        /*
        DTO sem validação, ou seja, o cliente pode enviar um campo nulo e isso vai sobrescrever o valor existente.
        */
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

    public void delete(final Long id){
        // Pra evitar exeptions/ problemas no banco / id nullo; verifica se a entidade existe antes de tentar deletar.
        final Product entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        this.repository.delete(entity);
    }

    /*  
    Esse metodo poderia ser colocado em um mapper separado. A pasta utils serve para isso.
    O ideal é criar uma classe lá como "ConverterHelper","ConverterUtil" etc 
    colocar esse tipo de método lá no modo static.
    Pois em outros lugares precisaremos fazer conversão para dto, exemplo -> Cart, Order.
    */
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