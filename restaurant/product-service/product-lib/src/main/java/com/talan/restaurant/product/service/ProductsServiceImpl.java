package com.talan.restaurant.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.talan.restaurant.product.dto.ProductDto;
import com.talan.restaurant.product.entity.Product;
import com.talan.restaurant.product.mapper.ProductMapper;
import com.talan.restaurant.product.repository.ProductPageableRepository;
import com.talan.restaurant.product.repository.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductsServiceImpl implements ProductsService {
	
	private static final String INVENTORY_SERVICE_URL = "lb://INVENTORY-SERVICE/api/inventory";
	
	private final ProductPageableRepository productsPageableRepository;
	private final ProductRepository productsRepository;
	private final ProductMapper productMapper;
	private final RestTemplate restTemplate;
	
	@Override
	public ProductDto add(ProductDto productDto) {
		Product productEntity = productMapper.toEntity(productDto);
		return productMapper.toDto(productsRepository.save(productEntity));
	}

	@Override
	public ProductDto findById(Long id) {
		return productMapper.toDto(productsRepository.findById(id).orElseThrow());
	}

	@Override
	public Page<ProductDto> getAll(Pageable pageable) {
		return productsPageableRepository.findAll(pageable)
					.map(productMapper::toDto);
	}

	@Override
	public ProductDto update(ProductDto product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		productsRepository.deleteById(id);
	}

	@Override
	@CircuitBreaker(name = "productAvailabilityCircuitBreaker", fallbackMethod = "getAvailabilityFallback")
	@Retryable(retryFor = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	public boolean getAvailability(String sku) {
		ResponseEntity<Boolean> response = restTemplate.exchange(
				INVENTORY_SERVICE_URL + "/" + sku,
				HttpMethod.GET,
				null,
				Boolean.class);
		return response.getBody();
	}
	
	public void getAvailabilityFallback(String sku, Throwable throwable) {
		log.error("Fallback method called when trying to get Availability for SKU: {}, due to: {}", sku, throwable.getMessage());
		throw new RuntimeException("Product availability check failed, please try again later.");
	}
	
}
