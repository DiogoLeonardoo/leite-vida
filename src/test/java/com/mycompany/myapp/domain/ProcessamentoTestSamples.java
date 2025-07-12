package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Processamento getProcessamentoSample1() {
        return new Processamento().id(1L).tecnicoResponsavel("tecnicoResponsavel1");
    }

    public static Processamento getProcessamentoSample2() {
        return new Processamento().id(2L).tecnicoResponsavel("tecnicoResponsavel2");
    }

    public static Processamento getProcessamentoRandomSampleGenerator() {
        return new Processamento().id(longCount.incrementAndGet()).tecnicoResponsavel(UUID.randomUUID().toString());
    }
}
