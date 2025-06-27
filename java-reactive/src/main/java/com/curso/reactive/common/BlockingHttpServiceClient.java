package com.curso.reactive.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlockingHttpServiceClient {
	private static final String BASE_URL = "http://localhost:7070";
	private final HttpClient client;
	
	public BlockingHttpServiceClient() {
		this.client = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1).build();
		log.info("BlockingHttpServiceClient initialized with base URL: {}", BASE_URL);
	}
	
	public String getProductName(int productId) throws IOException, InterruptedException {
		String url = BASE_URL + "/demo01/product/" + productId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new RuntimeException("HTTP error: " + response.statusCode());
        }
	}
}
