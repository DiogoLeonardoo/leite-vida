package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessamentoDTO.class);
        ProcessamentoDTO processamentoDTO1 = new ProcessamentoDTO();
        processamentoDTO1.setId(1L);
        ProcessamentoDTO processamentoDTO2 = new ProcessamentoDTO();
        assertThat(processamentoDTO1).isNotEqualTo(processamentoDTO2);
        processamentoDTO2.setId(processamentoDTO1.getId());
        assertThat(processamentoDTO1).isEqualTo(processamentoDTO2);
        processamentoDTO2.setId(2L);
        assertThat(processamentoDTO1).isNotEqualTo(processamentoDTO2);
        processamentoDTO1.setId(null);
        assertThat(processamentoDTO1).isNotEqualTo(processamentoDTO2);
    }
}
