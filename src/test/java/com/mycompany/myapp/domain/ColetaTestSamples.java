package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ColetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Coleta getColetaSample1() {
        return new Coleta().id(1L).localColeta("localColeta1").observacoes("observacoes1");
    }

    public static Coleta getColetaSample2() {
        return new Coleta().id(2L).localColeta("localColeta2").observacoes("observacoes2");
    }

    public static Coleta getColetaRandomSampleGenerator() {
        return new Coleta()
            .id(longCount.incrementAndGet())
            .localColeta(UUID.randomUUID().toString())
            .observacoes(UUID.randomUUID().toString());
    }
}
