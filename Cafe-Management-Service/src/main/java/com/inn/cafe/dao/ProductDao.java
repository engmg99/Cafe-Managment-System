package com.inn.cafe.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.entities.Product;
import com.inn.cafe.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Long> {
	@Query("SELECT new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p")
	public List<ProductWrapper> getAllProducts();

	@Modifying
	@Transactional
	@Query("Update Product p set p.status= :status where p.id=:id")
	public Integer updateProductStatus(@Param("status") String status, @Param("id") Long id);

	@Query("Select new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p where p.category.id=:id and p.status='true'")
	public List<ProductWrapper> getProductByCategory(@Param("id") Long id);

	@Query("Select new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p where p.id=:id and p.status='true'")
	public List<ProductWrapper> getProductById(@Param("id") Long id);
}
