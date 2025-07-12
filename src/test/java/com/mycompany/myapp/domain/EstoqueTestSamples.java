package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EstoqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Estoque getEstoqueSample1() {
        return new Estoque().id(1L).localArmazenamento("localArmazenamento1");
    }

    public static Estoque getEstoqueSample2() {
        return new Estoque().id(2L).localArmazenamento("localArmazenamento2");
    }

    public static Estoque getEstoqueRandomSampleGenerator() {
        return new Estoque().id(longCount.incrementAndGet()).localArmazenamento(UUID.randomUUID().toString());
    }
}
