package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.EstoqueAsserts.*;
import static com.mycompany.myapp.domain.EstoqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstoqueMapperTest {

    private EstoqueMapper estoqueMapper;

    @BeforeEach
    void setUp() {
        estoqueMapper = new EstoqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEstoqueSample1();
        var actual = estoqueMapper.toEntity(estoqueMapper.toDto(expected));
        assertEstoqueAllPropertiesEquals(expected, actual);
    }
}
