package com.curso.reactive.sec02.practica;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec02Practica {

	public static void main(String[] args) {
		var fileService = new FileServiceImpl();
		
		var fileName = "file.txt";
		var content = "Hola, esto es un ejemplo de la primera práctica";
		
//		fileService.write("file.txt", "Hola, esto es un ejemplo de la primera práctica")
//				.subscribe(Util.createSubscriber());
//
//		fileService.read("file.txt").subscribe(Util.createSubscriber());
//
//		fileService.delete("file.txt").subscribe(Util.createSubscriber());
		
		fileService.write(fileName, "Hola, esto es un ejemplo de la primera práctica")
			.doOnSuccess(v -> log.info("Escritura completada!"))
			.then(fileService.read(fileName))
			.doOnNext(c -> log.info("Contenido leído: {}", c))
			.then(fileService.delete(fileName))
			.doOnSuccess(v -> log.info("Borrado completado!"))
			.doOnError(ex -> log.error("Error: {}", ex.getMessage(), ex))
			.subscribe(Util.createSubscriber())
			;
		
		Util.sleepSeconds(2);
	}

}
