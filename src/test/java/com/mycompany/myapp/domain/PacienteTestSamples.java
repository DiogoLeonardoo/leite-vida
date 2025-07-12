package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente()
            .id(1L)
            .nome("nome1")
            .registroHospitalar("registroHospitalar1")
            .idadeGestacional(1)
            .condicaoClinica("condicaoClinica1")
            .nomeResponsavel("nomeResponsavel1")
            .cpfResponsavel("cpfResponsavel1")
            .telefoneResponsavel("telefoneResponsavel1")
            .parentescoResponsavel("parentescoResponsavel1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente()
            .id(2L)
            .nome("nome2")
            .registroHospitalar("registroHospitalar2")
            .idadeGestacional(2)
            .condicaoClinica("condicaoClinica2")
            .nomeResponsavel("nomeResponsavel2")
            .cpfResponsavel("cpfResponsavel2")
            .telefoneResponsavel("telefoneResponsavel2")
            .parentescoResponsavel("parentescoResponsavel2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .registroHospitalar(UUID.randomUUID().toString())
            .idadeGestacional(intCount.incrementAndGet())
            .condicaoClinica(UUID.randomUUID().toString())
            .nomeResponsavel(UUID.randomUUID().toString())
            .cpfResponsavel(UUID.randomUUID().toString())
            .telefoneResponsavel(UUID.randomUUID().toString())
            .parentescoResponsavel(UUID.randomUUID().toString());
    }
}
