package com.inn.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.entities.CustomerBill;

public interface CustomerBillDao extends JpaRepository<CustomerBill, Long> {

	@Query("Select b from CustomerBill b order by b.id desc")
	List<CustomerBill> getAllBills();

	@Query("Select b from CustomerBill b where b.createdBy=:username order by b.id desc")
	List<CustomerBill> getAllBillsByUsername(@Param("username") String currentLoggedInUser);

}
