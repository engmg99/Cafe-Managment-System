package com.inn.cafe.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.pdfbox.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.CustomerBillDao;
import com.inn.cafe.entities.CustomerBill;
import com.inn.cafe.jwt.JWTFilter;
import com.inn.cafe.service.CustomerBillService;
import com.inn.cafe.util.CafeUtils;
import com.inn.cafe.util.PDFUtils;

@Service
public class CustomerBillServiceImpl implements CustomerBillService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerBillServiceImpl.class);

	@Autowired
	private CustomerBillDao billDao;

	@Autowired
	private JWTFilter jwtFilter;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> reqMap) {
		try {
			String fileName;
			if (jwtFilter.isAdmin()) {
				if (validateBillReqMap(reqMap)) {
					if (reqMap.containsKey("isGenerate") && !(Boolean) reqMap.get("isGenerate")) {
						fileName = (String) reqMap.get("uuid");
					} else {
						fileName = CafeUtils.getUUID();
						reqMap.put("uuid", fileName);
						insertBill(reqMap);
					}
					PDFUtils.generatePDF(reqMap, "Cafe Management System", fileName);
					return new ResponseEntity<String>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateBillReqMap(Map<String, Object> reqMap) {
		return reqMap.containsKey("name") && reqMap.containsKey("contactNumber") && reqMap.containsKey("email")
				&& reqMap.containsKey("paymentMethod") && reqMap.containsKey("productDetails")
				&& reqMap.containsKey("totalAmount");
	}

	private void insertBill(Map<String, Object> reqMap) {
		try {
			CustomerBill bill = new CustomerBill();
			bill.setUuid(reqMap.get("uuid") + "");
			bill.setName(reqMap.get("name") + "");
			bill.setEmail(reqMap.get("email") + "");
			bill.setContactNumber(reqMap.get("contactNumber") + "");
			bill.setPaymentMethod(reqMap.get("paymentMethod") + "");
			bill.setTotalAmount(Long.parseLong(reqMap.get("totalAmount") + ""));
//			bill.setProductDetails((String) reqMap.get("productDetails"));
			bill.setCreatedBy(jwtFilter.getCurrentLoggedInUser());
			billDao.save(bill);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ResponseEntity<List<CustomerBill>> getAllBills() {
		List<CustomerBill> list = new ArrayList<>();
		try {
			if (jwtFilter.isAdmin()) {
				list = billDao.getAllBills();
			} else {
				list = billDao.getAllBillsByUsername(jwtFilter.getCurrentLoggedInUser());
			}
			return new ResponseEntity<List<CustomerBill>>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<CustomerBill>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<byte[]> getPDF(Map<String, Object> reqMap) {
		LOGGER.info("Inside getPDF: " + reqMap);
		try {
			byte[] byteArray = new byte[0];
			if (!reqMap.containsKey("uuid") && validateBillReqMap(reqMap)) {
				return new ResponseEntity<byte[]>(byteArray, HttpStatus.BAD_REQUEST);
			}
			String filePath = CafeConstants.STORE_LOCATION + "\\" + (String) reqMap.get("uuid") + ".pdf";
			boolean fileExists = CafeUtils.isFileExist(filePath);
			LOGGER.info("CafeUtils.isFileExist: " + fileExists);
			if (fileExists) {
				byteArray = getBytesArray(filePath);
			} else {
				reqMap.put("isGenerate", false);
				generateReport(reqMap);
				byteArray = getBytesArray(filePath);
			}
			return new ResponseEntity<byte[]>(byteArray, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getBytesArray(String filePath) throws IOException {
		File initialFile = new File(filePath);
		InputStream targetStream = new FileInputStream(initialFile);
		byte[] byteArray = IOUtils.toByteArray(targetStream);
		targetStream.close();
		return byteArray;

	}

	@Override
	public ResponseEntity<String> deleteBill(Long id) {
		try {
			Optional<CustomerBill> optional = billDao.findById(id);
			if (!optional.isEmpty()) {
				billDao.deleteById(id);
				return CafeUtils.getResponseEntity("Bill Deleted Successfully.", HttpStatus.OK);
			}
			return CafeUtils.getResponseEntity("Bill id does not exist.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
