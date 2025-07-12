package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProcessamentoAsserts.*;
import static com.mycompany.myapp.domain.ProcessamentoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessamentoMapperTest {

    private ProcessamentoMapper processamentoMapper;

    @BeforeEach
    void setUp() {
        processamentoMapper = new ProcessamentoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProcessamentoSample1();
        var actual = processamentoMapper.toEntity(processamentoMapper.toDto(expected));
        assertProcessamentoAllPropertiesEquals(expected, actual);
    }
}
