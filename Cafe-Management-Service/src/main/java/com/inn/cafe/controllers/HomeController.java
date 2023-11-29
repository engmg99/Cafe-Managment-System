package com.inn.cafe.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.service.HomeService;

@RestController
@RequestMapping("/dashboard")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping("/details")
	public ResponseEntity<Map<String, Object>> getCount() {
		try {
			return homeService.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	@PostMapping("/add")
//	public ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> reqMap) {
//		try {
//			return categoryService.addNewCategory(reqMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}
