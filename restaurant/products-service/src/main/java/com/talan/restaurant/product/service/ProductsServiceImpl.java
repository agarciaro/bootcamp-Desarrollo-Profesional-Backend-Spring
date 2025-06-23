package com.talan.restaurant.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.talan.restaurant.product.dto.ProductDto;
import com.talan.restaurant.product.entity.Product;
import com.talan.restaurant.product.mapper.ProductMapper;
import com.talan.restaurant.product.repository.ProductPageableRepository;
import com.talan.restaurant.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductsServiceImpl implements ProductsService {
	
	private final ProductPageableRepository productsPageableRepository;
	private final ProductRepository productsRepository;
	private final ProductMapper productMapper;
	
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
	
}
