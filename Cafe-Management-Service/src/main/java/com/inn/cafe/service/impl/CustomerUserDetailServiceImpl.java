package com.inn.cafe.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inn.cafe.dao.UserDao;
import com.inn.cafe.entities.CafeUser;
import com.inn.cafe.service.CustomerUserDetailService;

@Service
public class CustomerUserDetailServiceImpl implements CustomerUserDetailService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerUserDetailServiceImpl.class);

	@Autowired
	private UserDao userDao;

	private CafeUser cafeUserDetails;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		LOG.info("Inside CustomerUserDetailServiceImpl.loadUserByUsername{}", email);
		cafeUserDetails = userDao.findByEmailId(email);
		if (cafeUserDetails != null) {
			// this below is (Spring-Security) User Object and its constructor needs the
			// username, pwd, and roles. So we provided the username as email, pwd and empty
			// arrayList because we're getting roles from DB only
			return new User(cafeUserDetails.getEmail(), cafeUserDetails.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with email:" + email);
		}
	}

	public CafeUser getCafeUserDetails() {
		CafeUser cafeUser = cafeUserDetails;
		cafeUser.setPassword(null); // this additional step is done so that the user password will not get exposed
		return cafeUserDetails;
	}

}
