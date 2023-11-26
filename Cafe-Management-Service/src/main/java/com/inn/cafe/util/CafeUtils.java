package com.inn.cafe.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
	private CafeUtils() {

	}

	public static ResponseEntity<String> getResponseEntity(String resMsg, HttpStatus status) {
		return new ResponseEntity<String>("{\"message\":\"" + resMsg + "\"}", status);
	}
}
