package com.inn.cafe.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.entities.Category;
import com.inn.cafe.jwt.JWTFilter;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.util.CafeUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private JWTFilter jwtFilter;

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> reqMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(reqMap, false)) {
					categoryDao.save(getCategoryFromMap(reqMap, false));
					return CafeUtils.getResponseEntity("Category Added Successfully.", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
			}
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> reqMap, boolean validateId) {
		if (reqMap.containsKey("name")) {
			if (reqMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> reqMap, boolean isUpdate) {
		Category categoryObj = new Category();
		if (isUpdate) {
			categoryObj.setId(Long.parseLong(reqMap.get("id")));
		}
		categoryObj.setName(reqMap.get("name"));
		return categoryObj;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategories(String filterValue) {
		try {
			if (filterValue != null && !filterValue.isEmpty() && filterValue.equalsIgnoreCase("true")) {
				return new ResponseEntity<List<Category>>(categoryDao.getAllcategories(), HttpStatus.OK);
			}
			return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> reqMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(reqMap, true)) {
					Optional<Category> existingCategory = categoryDao.findById(Long.parseLong(reqMap.get("id")));
					if (!existingCategory.isEmpty()) {
						categoryDao.save(getCategoryFromMap(reqMap, true));
						return CafeUtils.getResponseEntity("Category Updated Successfully.", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseEntity("Catergory does not exist with id: " + reqMap.get("id"),
								HttpStatus.BAD_REQUEST);
					}
				} else {
					return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
			}
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
