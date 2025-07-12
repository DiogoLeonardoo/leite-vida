package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DistribuicaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistribuicaoDTO.class);
        DistribuicaoDTO distribuicaoDTO1 = new DistribuicaoDTO();
        distribuicaoDTO1.setId(1L);
        DistribuicaoDTO distribuicaoDTO2 = new DistribuicaoDTO();
        assertThat(distribuicaoDTO1).isNotEqualTo(distribuicaoDTO2);
        distribuicaoDTO2.setId(distribuicaoDTO1.getId());
        assertThat(distribuicaoDTO1).isEqualTo(distribuicaoDTO2);
        distribuicaoDTO2.setId(2L);
        assertThat(distribuicaoDTO1).isNotEqualTo(distribuicaoDTO2);
        distribuicaoDTO1.setId(null);
        assertThat(distribuicaoDTO1).isNotEqualTo(distribuicaoDTO2);
    }
}
