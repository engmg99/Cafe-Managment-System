package com.inn.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.entities.CafeUser;

public interface UserService {
	public ResponseEntity<String> signUp(Map<String, String> reqMap);

	public ResponseEntity<String> login(CafeUser user);

	public ResponseEntity<List<CafeUser>> getAllCafeUsers();

	public ResponseEntity<String> updateCafeUser(CafeUser user);

	public ResponseEntity<String> checkToken();

	public ResponseEntity<String> changeUserPassword(Map<String, String> reqMap);

	public ResponseEntity<String> userForgetPassword(Map<String, String> reqMap);
}
