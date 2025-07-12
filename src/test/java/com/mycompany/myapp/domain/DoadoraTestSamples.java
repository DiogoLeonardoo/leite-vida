package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DoadoraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Doadora getDoadoraSample1() {
        return new Doadora()
            .id(1L)
            .nome("nome1")
            .cartaoSUS("cartaoSUS1")
            .cpf("cpf1")
            .cep("cep1")
            .estado("estado1")
            .cidade("cidade1")
            .endereco("endereco1")
            .telefone("telefone1")
            .profissao("profissao1");
    }

    public static Doadora getDoadoraSample2() {
        return new Doadora()
            .id(2L)
            .nome("nome2")
            .cartaoSUS("cartaoSUS2")
            .cpf("cpf2")
            .cep("cep2")
            .estado("estado2")
            .cidade("cidade2")
            .endereco("endereco2")
            .telefone("telefone2")
            .profissao("profissao2");
    }

    public static Doadora getDoadoraRandomSampleGenerator() {
        return new Doadora()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cartaoSUS(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .cep(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .profissao(UUID.randomUUID().toString());
    }
}
