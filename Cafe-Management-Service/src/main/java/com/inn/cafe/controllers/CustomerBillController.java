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
import com.inn.cafe.entities.CustomerBill;
import com.inn.cafe.service.CustomerBillService;
import com.inn.cafe.util.CafeUtils;

@RestController
@RequestMapping("/customer-bill")
public class CustomerBillController {

	@Autowired
	private CustomerBillService billService;

	@PostMapping("/generate-report")
	public ResponseEntity<String> generateReport(@RequestBody(required = true) Map<String, Object> reqMap) {
		try {
			return billService.generateReport(reqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/get-bills")
	public ResponseEntity<List<CustomerBill>> getAllBills() {
		try {
			return billService.getAllBills();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<CustomerBill>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("get-pdf")
	public ResponseEntity<byte[]> getPdf(@RequestBody(required = true) Map<String, Object> reqMap) {
		try {
			return billService.getPDF(reqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("delete/{id}")
	public ResponseEntity<String> getPdf(@PathVariable Long id) {
		try {
			return billService.deleteBill(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
