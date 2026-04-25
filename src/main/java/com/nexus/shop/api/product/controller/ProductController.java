package com.nexus.shop.api.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.nexus.shop.api.product.service.ProductService;
import com.nexus.shop.model.ApiResponse;
import com.nexus.shop.model.product.dto.ProductUpdateDTO;
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
        // Esse formato é ideal pra lidar com erros de validação e exceções.
        // Assim já retorna o status correto e uma mensagem de erro amigável :)

        // O que fiz foi criar uma classe ApiResponse genérica pra padronizar as respostas da API, tanto de sucesso quanto de erro.
        // Agora a gente retorna sempre essa classe, insere o objeto (criado, requisitado, etc) e uma mensagem de sucesso ou erro.
        try {
            // Em qualquer variável que não será modificada, é interessante usar o modificador final. Isso ajuda a evitar bugs acidentais, alem de deixar claro que aquela variável não deve ser reatribuída. 
            // Também diminui a compelxidade do codigo + diminui uso de memória, já que o compilador pode otimizar melhor o código.
            final ProductResponseDTO response = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(response, "Produto criado com sucesso"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, "Erro interno ao criar produto"));
        }
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