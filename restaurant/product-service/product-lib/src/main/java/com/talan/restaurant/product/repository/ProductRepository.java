package com.talan.restaurant.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talan.restaurant.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
}
