package com.curso.reactive.sec04.helper;

import java.util.function.Consumer;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

@Slf4j
public class NameGenerator implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    @Override
    public void accept(FluxSink<String> stringFluxSink) {
//    	log.info("FluxSink asignado");
        this.sink = stringFluxSink;
    }

    public void generate(){
        this.sink.next(Util.faker().name().firstName());
    }

}
