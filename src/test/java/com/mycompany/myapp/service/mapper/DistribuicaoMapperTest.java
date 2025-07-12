package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.DistribuicaoAsserts.*;
import static com.mycompany.myapp.domain.DistribuicaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistribuicaoMapperTest {

    private DistribuicaoMapper distribuicaoMapper;

    @BeforeEach
    void setUp() {
        distribuicaoMapper = new DistribuicaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDistribuicaoSample1();
        var actual = distribuicaoMapper.toEntity(distribuicaoMapper.toDto(expected));
        assertDistribuicaoAllPropertiesEquals(expected, actual);
    }
}
