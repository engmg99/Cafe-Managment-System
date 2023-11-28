package com.inn.cafe.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.util.CafeUtils;
import com.inn.cafe.wrapper.ProductWrapper;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/")
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {
			return productService.getAllProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String, String> reqMap) {
		try {
			return productService.addNewProduct(reqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> reqMap) {
		try {
			return productService.updateProduct(reqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		try {
			return productService.deleteProduct(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/update-status")
	public ResponseEntity<String> updateProductStatus(@RequestBody(required = true) Map<String, String> reqMap) {
		try {
			return productService.updateProductStatus(reqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/getByCategory/{categoryId}")
	public ResponseEntity<List<ProductWrapper>> getProductsByCategory(@PathVariable("categoryId") Long id) {
		try {
			return productService.getProductsByCategory(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<List<ProductWrapper>> getProductsById(@PathVariable Long id) {
		try {
			return productService.getProductById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
