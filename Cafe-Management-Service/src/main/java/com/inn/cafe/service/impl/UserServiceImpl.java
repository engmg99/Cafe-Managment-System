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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.entities.CafeUser;
import com.inn.cafe.jwt.JWTFilter;
import com.inn.cafe.jwt.JWTUtils;
import com.inn.cafe.service.UserService;
import com.inn.cafe.util.CafeUtils;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private CustomerUserDetailServiceImpl customerUserDetailService;

	@Autowired
	private JWTUtils utils;

	@Autowired
	private JWTFilter jwtFilter;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> reqMap) {
		try {
			logger.info("Inside UserServiceImpl.SignUp: ", reqMap);
			if (validateSignUpMap(reqMap)) {
				CafeUser userObj = userDao.findByEmailId(reqMap.get("email"));
				if (userObj == null) {
					userDao.save(getUserFromMap(reqMap));
					return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e + "");
			return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean validateSignUpMap(Map<String, String> reqMap) {
		return reqMap.containsKey("name") && reqMap.containsKey("contactNumber") && reqMap.containsKey("email")
				&& reqMap.containsKey("password");
	}

	private CafeUser getUserFromMap(Map<String, String> reqMap) {
		CafeUser userObj = new CafeUser();
		userObj.setName(reqMap.get("name"));
		userObj.setContactNumber(reqMap.get("contactNumber"));
		userObj.setEmail(reqMap.get("email"));
		userObj.setPassword(passwordEncoder.encode(reqMap.get("password")));
		userObj.setStatus("false");
		userObj.setRole("user");
		return userObj;
	}

	@Override
	public ResponseEntity<String> login(CafeUser user) {
		logger.info("Inside UserServiceImpl.login");
		try {
			// authenticating the email and pwd sent by user
			Authentication auth = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			// if authenticated and if User Status is Active then generate the JWT token
			if (auth.isAuthenticated()) {
				CafeUser authenticatedUser = customerUserDetailService.getCafeUserDetails();
				if (authenticatedUser.getStatus().equalsIgnoreCase(CafeConstants.ACTIVE_USER)) {
					String tokenGenerated = utils.generateToken(authenticatedUser.getEmail(),
							authenticatedUser.getRole());
					return new ResponseEntity<String>("{\"token\":\"" + tokenGenerated + "\"}", HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"Wait for Admin approval\"}",
							HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex + "");
		}
		return new ResponseEntity<String>("{\"message\":\"Bad Credentials.\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<CafeUser>> getAllCafeUsers() {
		try {
			if (jwtFilter.isAdmin()) {
				List<Object[]> list = userDao.getAllCafeUsers();
				List<CafeUser> cafeUserList = new ArrayList<>();
				CafeUser cafeUserObj = null;
				for (Object[] obj : list) {
					cafeUserObj = new CafeUser(Long.parseLong(obj[0] + ""), obj[1] + "", obj[2] + "", obj[3] + "",
							obj[4] + "", obj[5] + "");
					cafeUserList.add(cafeUserObj);
				}
				System.out.println(list);
				return new ResponseEntity<List<CafeUser>>(cafeUserList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<CafeUser>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e + "");
		}
		return new ResponseEntity<List<CafeUser>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCafeUser(CafeUser user) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional<CafeUser> optionalCUserObj = userDao.findById(user.getId());
				if (!optionalCUserObj.isEmpty()) {
					userDao.updateCafeUserStatus(user.getStatus(), user.getId());
					return CafeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("User ID does not exist", HttpStatus.OK);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
