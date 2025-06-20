package com.talan.restaurant.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.talan.restaurant.product.dto.ProductDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductDto> getAllProducts() {
		log.info("Calling getAllProducts");
		return new ArrayList<>();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductDto addProduct(@RequestBody ProductDto product) {
		log.info("Calling addProduct: {}", product);
		return new ProductDto(1l, "1", "Product 1", "Product Descrption 1", 15.0);
	}
	
	
}
