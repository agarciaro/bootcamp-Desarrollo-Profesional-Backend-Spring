package com.curso.reactive.sec02.practica;

import java.nio.file.Files;
import java.nio.file.Path;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FileServiceImpl implements FileService {

	private static final Path PATH = Path.of("src/main/resources/sec02");

	@Override
	public Mono<String> read(String fileName) {
		return Mono.fromCallable(() -> {
			Path filePath = PATH.resolve(fileName);
			return Files.readString(filePath);
		}).subscribeOn(Schedulers.boundedElastic()) // Ejecuta en un hilo no bloqueante
				.onErrorMap(ex -> new RuntimeException("Error reading file " + fileName, ex));
	}

	@Override
	public Mono<Void> write(String fileName, String content) {
		return Mono.fromRunnable(() -> {
			try {
				Path filePath = PATH.resolve(fileName);
				Files.writeString(filePath, content);
			} catch (Exception e) {
				throw new RuntimeException("Error writing to file " + fileName, e);
			}
		})
		.subscribeOn(Schedulers.boundedElastic())
		.then();
		
	}

	@Override
	public Mono<Void> delete(String fileName) {
		return Mono.fromRunnable(() -> {
			try {
				Path filePath = PATH.resolve(fileName);
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				throw new RuntimeException("Error deleting to file " + fileName, e);
			}
		})
		.subscribeOn(Schedulers.boundedElastic())
		.then();
	}

}
