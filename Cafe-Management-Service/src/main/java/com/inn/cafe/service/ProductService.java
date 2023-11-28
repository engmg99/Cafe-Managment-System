package com.inn.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.wrapper.ProductWrapper;

public interface ProductService {

	public ResponseEntity<String> addNewProduct(Map<String, String> reqMap);

	public ResponseEntity<List<ProductWrapper>> getAllProducts();

	public ResponseEntity<String> updateProduct(Map<String, String> reqMap);

}
