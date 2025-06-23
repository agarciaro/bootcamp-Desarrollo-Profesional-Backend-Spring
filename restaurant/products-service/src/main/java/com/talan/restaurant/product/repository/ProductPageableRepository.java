package com.talan.restaurant.product.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.talan.restaurant.product.entity.Product;

public interface ProductPageableRepository extends PagingAndSortingRepository<Product, Long> {
	
	
}
