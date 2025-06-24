package com.talan.restaurant.product.mapper;

import org.mapstruct.Mapper;

import com.talan.restaurant.product.dto.ProductDto;
import com.talan.restaurant.product.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	
	Product toEntity(ProductDto productDto);
	ProductDto toDto(Product product);
	
}
