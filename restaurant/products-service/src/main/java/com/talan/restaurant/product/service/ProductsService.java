package com.talan.restaurant.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.talan.restaurant.product.dto.ProductDto;

public interface ProductsService {
	
	public ProductDto add(ProductDto product);
	public ProductDto findById(Long id);
	public Page<ProductDto> getAll(Pageable pageable);
	public ProductDto update(ProductDto product);
	public void delete(Long id);
	
	public boolean getAvailability(String sku);
	
}