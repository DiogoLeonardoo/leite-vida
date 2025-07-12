package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstoqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstoqueDTO.class);
        EstoqueDTO estoqueDTO1 = new EstoqueDTO();
        estoqueDTO1.setId(1L);
        EstoqueDTO estoqueDTO2 = new EstoqueDTO();
        assertThat(estoqueDTO1).isNotEqualTo(estoqueDTO2);
        estoqueDTO2.setId(estoqueDTO1.getId());
        assertThat(estoqueDTO1).isEqualTo(estoqueDTO2);
        estoqueDTO2.setId(2L);
        assertThat(estoqueDTO1).isNotEqualTo(estoqueDTO2);
        estoqueDTO1.setId(null);
        assertThat(estoqueDTO1).isNotEqualTo(estoqueDTO2);
    }
}
