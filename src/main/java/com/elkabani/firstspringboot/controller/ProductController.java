package com.elkabani.firstspringboot.controller;

import com.elkabani.firstspringboot.dto.ProductDto;
import com.elkabani.firstspringboot.mapper.ProductMapper;
import com.elkabani.firstspringboot.model.Category;
import com.elkabani.firstspringboot.model.Product;
import com.elkabani.firstspringboot.repository.CategoryRepository;
import com.elkabani.firstspringboot.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    public ProductController(ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             ProductMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    // GET /products?categoryId=...
    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Integer categoryId) {
        List<Product> products;
        if (categoryId == null) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findAllByCategory_Id(categoryId);
        }
        return mapper.toDtoList(products);
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapper.toDto(opt.get()));
    }

    // POST /products
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto, UriComponentsBuilder uriBuilder) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            Optional<Category> catOpt = categoryRepository.findById(dto.getCategoryId());
            if (catOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            category = catOpt.get();
        }

        Product p = mapper.toEntity(dto);
        p.setCategory(category);
        Product saved = productRepository.save(p);
        URI location = uriBuilder.path("/products/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(mapper.toDto(saved));
    }

    // PUT /products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Product product = opt.get();

        // handle category if provided (null -> don't change)
        if (dto.getCategoryId() != null) {
            Optional<Category> catOpt = categoryRepository.findById(dto.getCategoryId());
            if (catOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            product.setCategory(catOpt.get());
        }

        mapper.updateFromDto(dto, product);
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) return ResponseEntity.notFound().build();
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
