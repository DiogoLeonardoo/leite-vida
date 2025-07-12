package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.DoadoraAsserts.*;
import static com.mycompany.myapp.domain.DoadoraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoadoraMapperTest {

    private DoadoraMapper doadoraMapper;

    @BeforeEach
    void setUp() {
        doadoraMapper = new DoadoraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDoadoraSample1();
        var actual = doadoraMapper.toEntity(doadoraMapper.toDto(expected));
        assertDoadoraAllPropertiesEquals(expected, actual);
    }
}
