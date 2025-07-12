package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ColetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ColetaDTO.class);
        ColetaDTO coletaDTO1 = new ColetaDTO();
        coletaDTO1.setId(1L);
        ColetaDTO coletaDTO2 = new ColetaDTO();
        assertThat(coletaDTO1).isNotEqualTo(coletaDTO2);
        coletaDTO2.setId(coletaDTO1.getId());
        assertThat(coletaDTO1).isEqualTo(coletaDTO2);
        coletaDTO2.setId(2L);
        assertThat(coletaDTO1).isNotEqualTo(coletaDTO2);
        coletaDTO1.setId(null);
        assertThat(coletaDTO1).isNotEqualTo(coletaDTO2);
    }
}
