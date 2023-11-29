package com.inn.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.entities.CustomerBill;

public interface CustomerBillService {

	public ResponseEntity<String> generateReport(Map<String, Object> reqMap);

	public ResponseEntity<List<CustomerBill>> getAllBills();

	public ResponseEntity<byte[]> getPDF(Map<String, Object> reqMap);

	public ResponseEntity<String> deleteBill(Long id);

}
