package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DistribuicaoTestSamples.*;
import static com.mycompany.myapp.domain.EstoqueTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DistribuicaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distribuicao.class);
        Distribuicao distribuicao1 = getDistribuicaoSample1();
        Distribuicao distribuicao2 = new Distribuicao();
        assertThat(distribuicao1).isNotEqualTo(distribuicao2);

        distribuicao2.setId(distribuicao1.getId());
        assertThat(distribuicao1).isEqualTo(distribuicao2);

        distribuicao2 = getDistribuicaoSample2();
        assertThat(distribuicao1).isNotEqualTo(distribuicao2);
    }

    @Test
    void estoqueTest() {
        Distribuicao distribuicao = getDistribuicaoRandomSampleGenerator();
        Estoque estoqueBack = getEstoqueRandomSampleGenerator();

        distribuicao.setEstoque(estoqueBack);
        assertThat(distribuicao.getEstoque()).isEqualTo(estoqueBack);

        distribuicao.estoque(null);
        assertThat(distribuicao.getEstoque()).isNull();
    }

    @Test
    void pacienteTest() {
        Distribuicao distribuicao = getDistribuicaoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        distribuicao.setPaciente(pacienteBack);
        assertThat(distribuicao.getPaciente()).isEqualTo(pacienteBack);

        distribuicao.paciente(null);
        assertThat(distribuicao.getPaciente()).isNull();
    }
}
