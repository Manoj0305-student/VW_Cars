package com.vw;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.vw.dao")
//@ComponentScan(basePackages = "com.vw")
//@EntityScan(basePackages = "com.vw.entities")
public class CarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsApplication.class, args);
		
	}
}
