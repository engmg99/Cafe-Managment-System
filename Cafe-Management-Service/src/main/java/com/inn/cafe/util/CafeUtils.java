package com.inn.cafe.util;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class CafeUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CafeUtils.class);

	private CafeUtils() {

	}

	public static ResponseEntity<String> getResponseEntity(String resMsg, HttpStatus status) {
		return new ResponseEntity<String>("{\"message\":\"" + resMsg + "\"}", status);
	}

	public static String getUUID() {
		Date date = new Date();
		long time = date.getTime();
		return "BILL-" + time;
	}

	public static JSONArray getJsonArrayFromString(String data) throws JSONException {
		return new JSONArray(data);
	}

	public static Map<String, Object> getMapFromJson(String data) {
		if (!Strings.isNullOrEmpty(data)) {
			return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
				private static final long serialVersionUID = 1L;
			}.getType());
		}
		return new HashMap<>();
	}

	public static Boolean isFileExist(String path) {
		LOGGER.info("CafeUtils.isFileExist");
		try {
			File file = new File(path);
			return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
}
