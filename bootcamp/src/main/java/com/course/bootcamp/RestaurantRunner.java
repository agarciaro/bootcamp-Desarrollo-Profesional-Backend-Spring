package com.course.bootcamp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.course.bootcamp.model.Restaurant;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
//@AllArgsConstructor
public class RestaurantRunner implements CommandLineRunner {
	
	//@Autowired
	final Restaurant restaurant;
	
	//@Autowired
	/*public RestaurantRunner(Restaurant restaurant) {
		this.restaurant = restaurant;
	}*/
	
	public static void main(String[] args) {
		SpringApplication.run(RestaurantRunner.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		restaurant.go();
		//restaurant.setName("New Restaurant Name");
	}

}
