package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ColetaTestSamples.*;
import static com.mycompany.myapp.domain.EstoqueTestSamples.*;
import static com.mycompany.myapp.domain.ProcessamentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Processamento.class);
        Processamento processamento1 = getProcessamentoSample1();
        Processamento processamento2 = new Processamento();
        assertThat(processamento1).isNotEqualTo(processamento2);

        processamento2.setId(processamento1.getId());
        assertThat(processamento1).isEqualTo(processamento2);

        processamento2 = getProcessamentoSample2();
        assertThat(processamento1).isNotEqualTo(processamento2);
    }

    @Test
    void estoqueTest() {
        Processamento processamento = getProcessamentoRandomSampleGenerator();
        Estoque estoqueBack = getEstoqueRandomSampleGenerator();

        processamento.setEstoque(estoqueBack);
        assertThat(processamento.getEstoque()).isEqualTo(estoqueBack);

        processamento.estoque(null);
        assertThat(processamento.getEstoque()).isNull();
    }

    @Test
    void coletaTest() {
        Processamento processamento = getProcessamentoRandomSampleGenerator();
        Coleta coletaBack = getColetaRandomSampleGenerator();

        processamento.setColeta(coletaBack);
        assertThat(processamento.getColeta()).isEqualTo(coletaBack);
        assertThat(coletaBack.getProcessamento()).isEqualTo(processamento);

        processamento.coleta(null);
        assertThat(processamento.getColeta()).isNull();
        assertThat(coletaBack.getProcessamento()).isNull();
    }
}
