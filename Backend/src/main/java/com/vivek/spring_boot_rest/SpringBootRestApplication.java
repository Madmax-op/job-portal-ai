package com.vivek.spring_boot_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringBootRestApplication {

	public static void main(String[] args) {
		System.out.println("Starting Job Portal Backend Application...");
		SpringApplication.run(SpringBootRestApplication.class, args);
		System.out.println("Job Portal Backend Application started successfully!");
	}

}