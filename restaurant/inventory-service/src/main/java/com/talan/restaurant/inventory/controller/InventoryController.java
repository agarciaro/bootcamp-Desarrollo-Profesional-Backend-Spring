package com.talan.restaurant.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {
	
	@GetMapping("/{sku}")
	@ResponseStatus(HttpStatus.OK)
	public boolean isInStock(@PathVariable String sku) {
		log.info("Checking inventory for sku: {}", sku);
		return true;
	}
	
}
