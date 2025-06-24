package com.talan.restaurant.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.talan.restaurant.product.dto.ProductDto;

//@FeignClient(name = "product-service", path = "/api/products")
public interface ProductControllerApi {
	
	@GetMapping
	Page<ProductDto> getAllProducts(Pageable pageable);
	
	@GetMapping("/{id}")
	ProductDto getProductById(Long id);
	
	@PostMapping
	ProductDto addProduct(ProductDto product);
	
	@PutMapping("/{id}")
	ProductDto updateProduct(Long id, ProductDto product);
	
	@DeleteMapping("/{id}")
	void deleteProduct(Long id);

	

}