package com.inn.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inn.cafe.entities.Product;
import com.inn.cafe.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Long> {
	@Query("SELECT new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p")
	public List<ProductWrapper> getAllProducts();
}
