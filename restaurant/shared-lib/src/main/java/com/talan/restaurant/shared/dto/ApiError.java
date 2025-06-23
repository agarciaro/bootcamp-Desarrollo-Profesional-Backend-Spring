package com.talan.restaurant.shared.dto;

import lombok.Builder;

@Builder
public record ApiError(
		int code,
		String message
) {}
