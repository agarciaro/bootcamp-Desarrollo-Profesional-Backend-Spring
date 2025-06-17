package com.course.bootcamp.builder;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
	private final UUID id;
    private final String name;
    private final String email;

   /* private CustomerDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
    }
    
    public static class Builder {
        private UUID id;
        private String name;
        private String email;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public CustomerDTO build() { return new CustomerDTO(this); }
    }
    */
    
}
