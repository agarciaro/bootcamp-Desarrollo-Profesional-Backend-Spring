package com.course.bootcamp.model;

import java.util.UUID;

public record Customer(UUID id, String name, String email, int age) {

}
