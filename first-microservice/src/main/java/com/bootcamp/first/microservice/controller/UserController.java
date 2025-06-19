package com.bootcamp.first.microservice.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;

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
		if(users.add(user) == false) {
			throw new RuntimeException("Duplicated user");
		}
		return users;
	}
	
	@GetMapping
	public Set<UserDto> getAllUsers(@RequestParam(required = false, name = "name", defaultValue = "") String userName) {
		return users.stream()
			.filter(user -> user.getName().toLowerCase().contains(userName.toLowerCase()))
			.collect(Collectors.toSet());
		//return users;
	}
	
	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable Integer id) {
		Optional<UserDto> userResponse = users.stream()
			.filter(user -> user != null && id.equals(user.getId()))
			.findFirst()
			;
		return userResponse.orElseThrow(() -> new UserNotFoundException(id));
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Integer id) {
		//TODO implement this
		throw new UnsupportedOperationException("this feature is not implemented yet");
	}
	
	
}
