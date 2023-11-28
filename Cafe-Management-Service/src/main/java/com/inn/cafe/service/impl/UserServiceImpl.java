package com.inn.cafe.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.inn.cafe.util.EmailUtils;
import com.inn.cafe.wrapper.CafeUserWrapper;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> reqMap) {
		try {
			LOGGER.info("Inside UserServiceImpl.SignUp: ", reqMap);
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
			LOGGER.error(e + "");
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
		LOGGER.info("Inside UserServiceImpl.login");
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
			LOGGER.error(ex + "");
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
			LOGGER.error(e + "");
		}
		return new ResponseEntity<List<CafeUser>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCafeUser(CafeUser user) {
		try {
			if (jwtFilter.isAdmin()) {
				CafeUser cUserObj = userDao.findById(user.getId()).get();
				if (cUserObj != null) {

					Integer result = userDao.updateCafeUserStatus(user.getStatus(), user.getId());
					LOGGER.info("Update Result: ", result);

					sendEmailToAllAdmins(user.getStatus(), cUserObj.getEmail(), userDao.getAllAdmins());

					return CafeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("User ID does not exist", HttpStatus.OK);
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

	private void sendEmailToAllAdmins(String status, String email, List<CafeUserWrapper> allAdmins) {
		List<String> allAdminsList = new ArrayList<>();
		for (CafeUserWrapper obj : allAdmins) {
			allAdminsList.add(obj.getEmail());
		}
		allAdminsList.remove(jwtFilter.getCurrentLoggedInUser());
		if (status != null && status.equalsIgnoreCase(CafeConstants.ACTIVE_USER)) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentLoggedInUser(), "Account Approved",
					"User:- " + email + "\n is approved by \n ADMIN:- " + jwtFilter.getCurrentLoggedInUser(),
					allAdminsList);
		} else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentLoggedInUser(), "Account Disabled",
					"User:- " + email + "\n is disabled by \n ADMIN:- " + jwtFilter.getCurrentLoggedInUser(),
					allAdminsList);
		}
	}

	@Override
	public ResponseEntity<String> checkToken() {
		try {
			return CafeUtils.getResponseEntity("true", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> changeUserPassword(Map<String, String> reqMap) {
		try {
			String emailIdOrUsername = jwtFilter.getCurrentLoggedInUser();
			CafeUser userFromDB = userDao.findByEmail(emailIdOrUsername);
			if (reqMap.isEmpty()) {
				return CafeUtils.getResponseEntity("Old and new Passwords are required", HttpStatus.BAD_REQUEST);
			}
			if (userFromDB != null && reqMap.containsKey("oldPassword") && reqMap.containsKey("newPassword")) {
				if (passwordEncoder.matches(reqMap.get("oldPassword"), userFromDB.getPassword())) {
					userFromDB.setPassword(passwordEncoder.encode(reqMap.get("newPassword")));
					userDao.save(userFromDB);
					return CafeUtils.getResponseEntity("Password updated successfully.", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> userForgetPassword(Map<String, String> reqMap) {
		try {
			if (!reqMap.isEmpty() && reqMap.containsKey("email") && !reqMap.get("email").isBlank()) {
				CafeUser userFromDB = userDao.findByEmail(reqMap.get("email"));
				if (userFromDB != null) {
					emailUtils.forgotPwdMail(userFromDB.getEmail(), "Credentials by CFM", userFromDB.getPassword());
					return CafeUtils.getResponseEntity("Check your mail for password", HttpStatus.OK);
				}
			}
			return CafeUtils.getResponseEntity("Email and email value is required", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e + "");
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
