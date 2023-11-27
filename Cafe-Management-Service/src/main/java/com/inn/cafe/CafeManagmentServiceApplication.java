package com.inn.cafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class CafeManagmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeManagmentServiceApplication.class, args);
	}

}
