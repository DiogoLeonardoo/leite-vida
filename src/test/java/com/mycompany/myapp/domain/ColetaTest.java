package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ColetaTestSamples.*;
import static com.mycompany.myapp.domain.DoadoraTestSamples.*;
import static com.mycompany.myapp.domain.ProcessamentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ColetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coleta.class);
        Coleta coleta1 = getColetaSample1();
        Coleta coleta2 = new Coleta();
        assertThat(coleta1).isNotEqualTo(coleta2);

        coleta2.setId(coleta1.getId());
        assertThat(coleta1).isEqualTo(coleta2);

        coleta2 = getColetaSample2();
        assertThat(coleta1).isNotEqualTo(coleta2);
    }

    @Test
    void processamentoTest() {
        Coleta coleta = getColetaRandomSampleGenerator();
        Processamento processamentoBack = getProcessamentoRandomSampleGenerator();

        coleta.setProcessamento(processamentoBack);
        assertThat(coleta.getProcessamento()).isEqualTo(processamentoBack);

        coleta.processamento(null);
        assertThat(coleta.getProcessamento()).isNull();
    }

    @Test
    void doadoraTest() {
        Coleta coleta = getColetaRandomSampleGenerator();
        Doadora doadoraBack = getDoadoraRandomSampleGenerator();

        coleta.setDoadora(doadoraBack);
        assertThat(coleta.getDoadora()).isEqualTo(doadoraBack);

        coleta.doadora(null);
        assertThat(coleta.getDoadora()).isNull();
    }
}
