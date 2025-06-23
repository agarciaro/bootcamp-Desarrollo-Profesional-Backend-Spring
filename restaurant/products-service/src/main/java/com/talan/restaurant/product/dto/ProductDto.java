package com.talan.restaurant.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A generic product")
public record ProductDto(
		@Schema(description = "The unique identification of the product", example = "1")
		Long id, 
		String sku, 
		String name, 
		String description, 
		Double price
) {

}
