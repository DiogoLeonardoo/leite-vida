package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ColetaAsserts.*;
import static com.mycompany.myapp.domain.ColetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColetaMapperTest {

    private ColetaMapper coletaMapper;

    @BeforeEach
    void setUp() {
        coletaMapper = new ColetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getColetaSample1();
        var actual = coletaMapper.toEntity(coletaMapper.toDto(expected));
        assertColetaAllPropertiesEquals(expected, actual);
    }
}
