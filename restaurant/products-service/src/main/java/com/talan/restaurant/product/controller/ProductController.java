package com.talan.restaurant.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.talan.restaurant.product.dto.ProductDto;
import com.talan.restaurant.product.service.ProductsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
	
	private final ProductsService productsService;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Page<ProductDto> getAllProducts(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		log.info("Calling getAllProducts");
		return productsService.getAll(pageable);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductDto getProductById(@PathVariable Long id) {
		log.info("Calling getProductById");
		return productsService.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductDto addProduct(@RequestBody ProductDto product) {
		log.info("Calling addProduct: {}", product);
		return productsService.add(product);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
		log.info("Calling updateProduct: {}", product);
		return productsService.add(product);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductDto> deleteProduct(@PathVariable Long id) {
		log.info("Calling deleteProduct");
		return productsService.getAll();
	}
	
	
}
