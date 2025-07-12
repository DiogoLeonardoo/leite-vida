package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoadoraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoadoraDTO.class);
        DoadoraDTO doadoraDTO1 = new DoadoraDTO();
        doadoraDTO1.setId(1L);
        DoadoraDTO doadoraDTO2 = new DoadoraDTO();
        assertThat(doadoraDTO1).isNotEqualTo(doadoraDTO2);
        doadoraDTO2.setId(doadoraDTO1.getId());
        assertThat(doadoraDTO1).isEqualTo(doadoraDTO2);
        doadoraDTO2.setId(2L);
        assertThat(doadoraDTO1).isNotEqualTo(doadoraDTO2);
        doadoraDTO1.setId(null);
        assertThat(doadoraDTO1).isNotEqualTo(doadoraDTO2);
    }
}
