package com.course.bootcamp.validations;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderDTO(@NotNull UUID id, @Min(0) double amount) {

}
