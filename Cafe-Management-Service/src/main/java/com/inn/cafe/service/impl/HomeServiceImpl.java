package com.inn.cafe.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.dao.CustomerBillDao;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CustomerBillDao billDao;
	@Autowired
	private ProductDao productDao;

	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		Map<String, Object> map = new HashMap<>();
		map.put("category", categoryDao.count());
		map.put("product", productDao.count());
		map.put("bill", billDao.count());
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

}
