package com.talan.restaurant.product.controller;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products REST API")
public class ProductController implements ProductControllerApi {
	
	private final ProductsService productsService;
	
	@Override
	@Operation(summary = "Get all paginated products")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
		@ApiResponse(responseCode = "500", description = "Generic unhandled error")
	})
	
	@ResponseStatus(HttpStatus.OK)
	public Page<ProductDto> getAllProducts(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		log.info("Calling getAllProducts");
		return productsService.getAll(pageable);
	}
	
	@Override
	@Operation(summary = "Get a product by its id")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Product not found")
	})
	@ResponseStatus(HttpStatus.OK)
	public ProductDto getProductById(@PathVariable Long id) {
		log.info("Calling getProductById");
		return productsService.findById(id);
	}
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductDto addProduct(@RequestBody ProductDto product) {
		log.info("Calling addProduct: {}", product);
		return productsService.add(product);
	}
	
	@Override
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
		log.info("Calling updateProduct: {}", product);
		return productsService.add(product);
	}
	
	@Override
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteProduct(@PathVariable Long id) {
		log.info("Calling deleteProduct");
		productsService.delete(id);
	}
	
	
}
