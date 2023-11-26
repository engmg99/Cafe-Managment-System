package com.inn.cafe.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.entities.CafeUser;
import com.inn.cafe.wrapper.CafeUserWrapper;

public interface UserDao extends JpaRepository<CafeUser, Long> {

	@Query("Select u from CafeUser u where u.email= :email")
	public CafeUser findByEmailId(@Param("email") String email);

	@Query(value = "Select user_id, name, email, contact_number, role, status from Cafe_User u where u.role='user'", nativeQuery = true)
	public List<Object[]> getAllCafeUsers();

	@Transactional
	@Modifying
	@Query(value = "Update cafe_user u set u.status=:status where u.user_id=:id", nativeQuery = true)
	public Integer updateCafeUserStatus(@Param("status") String status, @Param("id") Long id);

	@Query("select new com.inn.cafe.wrapper.CafeUserWrapper(u.id, u.name, u.contactNumber, u.email, u.role, u.status) from CafeUser u where u.role='admin'")
	public List<CafeUserWrapper> getAllAdmins();
}
