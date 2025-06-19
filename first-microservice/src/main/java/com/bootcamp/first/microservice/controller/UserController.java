package com.bootcamp.first.microservice.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.first.microservice.dto.UserDto;
import com.bootcamp.first.microservice.exception.UserNotFoundException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
	
	Set<UserDto> users = new HashSet<>();
	
	@PostMapping
	//@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Set<UserDto> addUser(@Valid @RequestBody UserDto user) {
		users.add(user);
		return users;
	}
	
	@GetMapping
	public Set<UserDto> getAllUsers() {
		return users;
	}
	
	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable Integer id) {
		Optional<UserDto> userResponse = users.stream()
			.filter(user -> user != null && id.equals(user.getId()))
			.findFirst()
			;
		return userResponse.orElseThrow(() -> new UserNotFoundException(id));
	}
	
}
