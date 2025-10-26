package com.elkabani.firstspringboot.mapper;

import com.elkabani.firstspringboot.dto.ProductDto;
import com.elkabani.firstspringboot.model.Category;
import com.elkabani.firstspringboot.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        Category c = product.getCategory();
        dto.setCategoryId(c != null ? c.getId() : null);
        return dto;
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;
        Product p = new Product();
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setDescription(dto.getDescription());
        // Do not set id here - creation
        if (dto.getCategoryId() != null) {
            Category c = new Category();
            c.setId(dto.getCategoryId());
            p.setCategory(c);
        }
        return p;
    }

    public void updateFromDto(ProductDto dto, Product product) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        // category handled in controller (validate existence) so not set here
    }
}
