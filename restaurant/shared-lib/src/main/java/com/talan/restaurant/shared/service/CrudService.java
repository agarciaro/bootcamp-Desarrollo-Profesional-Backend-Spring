package com.talan.restaurant.shared.service;

import java.util.List;

public interface CrudService<DTO> {
	
	List<DTO> getAll();
	DTO add(DTO dto);
	void delete();
	
}
