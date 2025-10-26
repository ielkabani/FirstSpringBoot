package com.elkabani.firstspringboot.repository;

import com.elkabani.firstspringboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByName(String name);

	// find products by the category's id (Category.id is Integer)
	List<Product> findAllByCategory_Id(Integer categoryId);

}
