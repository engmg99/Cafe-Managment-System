package com.inn.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.entities.Category;

public interface CategoryService {

	public ResponseEntity<String> addNewCategory(Map<String, String> reqMap);

	public ResponseEntity<List<Category>> getAllCategories(String filterValue);

	public ResponseEntity<String> updateCategory(Map<String, String> reqMap);

}
