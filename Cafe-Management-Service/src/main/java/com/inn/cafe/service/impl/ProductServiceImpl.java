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
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.entities.Category;
import com.inn.cafe.entities.Product;
import com.inn.cafe.jwt.JWTFilter;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.util.CafeUtils;
import com.inn.cafe.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;

	@Autowired
	private JWTFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> reqMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(reqMap, false)) {
					productDao.save(getProductFromMap(reqMap, false));
					return CafeUtils.getResponseEntity("Product Added Successfully.", HttpStatus.OK);
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

	private boolean validateProductMap(Map<String, String> reqMap, boolean validateId) {
		if (reqMap.containsKey("name")) {
			if (reqMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Product getProductFromMap(Map<String, String> reqMap, boolean isUpdate) {
		Category catergoryObj = new Category();
		catergoryObj.setId(Long.parseLong(reqMap.get("categoryId")));

		Product productObj = new Product();
		if (isUpdate) {
			productObj.setId(Long.parseLong(reqMap.get("id")));
		} else {
			productObj.setStatus("true");
		}
		productObj.setName(reqMap.get("name"));
		productObj.setCategory(catergoryObj);
		productObj.setDescription(reqMap.get("description"));
		productObj.setPrice(Long.parseLong(reqMap.get("price")));
		return productObj;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {
			return new ResponseEntity<List<ProductWrapper>>(productDao.getAllProducts(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> reqMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(reqMap, true)) {
					Optional<Product> existingProduct = productDao.findById(Long.parseLong(reqMap.get("id")));
					if (!existingProduct.isEmpty()) {
						Product productObj = getProductFromMap(reqMap, true);
						productObj.setStatus("true");
						productDao.save(productObj);
						return CafeUtils.getResponseEntity("Product Updated Successfully.", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseEntity("Product does not exist with id: " + reqMap.get("id"),
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
