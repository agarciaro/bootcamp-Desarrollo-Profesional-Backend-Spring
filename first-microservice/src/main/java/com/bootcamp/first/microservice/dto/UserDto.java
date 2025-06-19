package com.bootcamp.first.microservice.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
	
	//@JsonIgnore
	private int id;
	@NotBlank
	private String name;
	@Min(value = 18, message = "You must be greater than 18")
	private int age;
	//@JsonProperty("userOrder")
	//private Order order;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDto other = (UserDto) obj;
		return id == other.id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	
	
}
