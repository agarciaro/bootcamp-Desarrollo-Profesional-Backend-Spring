package com.course.bootcamp.model;

import java.util.UUID;

public record Order(UUID id, UUID customerId, Double amount, String status) {

}
