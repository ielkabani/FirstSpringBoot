package com.elkabani.firstspringboot;

import com.elkabani.firstspringboot.model.Category;
import com.elkabani.firstspringboot.model.Product;
import com.elkabani.firstspringboot.repository.CategoryRepository;
import com.elkabani.firstspringboot.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class FirstSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstSpringBootApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(CategoryRepository categoryRepo, ProductRepository productRepo) {
        return args -> {
            System.out.println("=== START DEMO ===");

            // A) Create product with NEW category
            Category electronics = categoryRepo.findByName("Electronics")
                    .orElseGet(() -> categoryRepo.save(new Category("Electronics")));
            Product camera = productRepo.save(new Product("Camera", new BigDecimal("399.99"), electronics));
            System.out.printf("A) Created category '%s' (id=%s) and product '%s' (id=%s)%n",
                    electronics.getName(), electronics.getId(), camera.getName(), camera.getId());

            // B) Create product for EXISTING category
            Category existing = categoryRepo.findByName("Electronics").orElseThrow();
            Product headphones = productRepo.save(new Product("Headphones", new BigDecimal("99.95"), existing));
            System.out.printf("B) Created product '%s' (id=%s) in category '%s'%n",
                    headphones.getName(), headphones.getId(), existing.getName());

            // List BEFORE delete
            System.out.println("Products BEFORE delete:");
            productRepo.findAll().forEach(p ->
                    System.out.printf(" - [%d] %s | %s | category=%s%n",
                            p.getId(), p.getName(), p.getPrice(), p.getCategory().getName())
            );

            // C) Delete Headphones
            productRepo.deleteById(headphones.getId());
            System.out.printf("C) Deleted product '%s' (id=%s)%n", "Headphones", headphones.getId());

            // List AFTER delete
            System.out.println("Products AFTER delete:");
            productRepo.findAll().forEach(p ->
                    System.out.printf(" - [%d] %s | %s | category=%s%n",
                            p.getId(), p.getName(), p.getPrice(), p.getCategory().getName())
            );

            System.out.println("=== END DEMO ===");
        };
    }
}
