package com.inn.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inn.cafe.entities.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {

	@Query("Select c from Category c") // select * from category
	public List<Category> getAllcategories();
}
