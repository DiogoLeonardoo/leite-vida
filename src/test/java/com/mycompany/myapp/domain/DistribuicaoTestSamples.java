package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DistribuicaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Distribuicao getDistribuicaoSample1() {
        return new Distribuicao()
            .id(1L)
            .responsavelEntrega("responsavelEntrega1")
            .responsavelRecebimento("responsavelRecebimento1")
            .observacoes("observacoes1");
    }

    public static Distribuicao getDistribuicaoSample2() {
        return new Distribuicao()
            .id(2L)
            .responsavelEntrega("responsavelEntrega2")
            .responsavelRecebimento("responsavelRecebimento2")
            .observacoes("observacoes2");
    }

    public static Distribuicao getDistribuicaoRandomSampleGenerator() {
        return new Distribuicao()
            .id(longCount.incrementAndGet())
            .responsavelEntrega(UUID.randomUUID().toString())
            .responsavelRecebimento(UUID.randomUUID().toString())
            .observacoes(UUID.randomUUID().toString());
    }
}
