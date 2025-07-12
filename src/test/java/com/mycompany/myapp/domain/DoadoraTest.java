package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ColetaTestSamples.*;
import static com.mycompany.myapp.domain.DoadoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DoadoraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doadora.class);
        Doadora doadora1 = getDoadoraSample1();
        Doadora doadora2 = new Doadora();
        assertThat(doadora1).isNotEqualTo(doadora2);

        doadora2.setId(doadora1.getId());
        assertThat(doadora1).isEqualTo(doadora2);

        doadora2 = getDoadoraSample2();
        assertThat(doadora1).isNotEqualTo(doadora2);
    }

    @Test
    void coletasTest() {
        Doadora doadora = getDoadoraRandomSampleGenerator();
        Coleta coletaBack = getColetaRandomSampleGenerator();

        doadora.addColetas(coletaBack);
        assertThat(doadora.getColetas()).containsOnly(coletaBack);
        assertThat(coletaBack.getDoadora()).isEqualTo(doadora);

        doadora.removeColetas(coletaBack);
        assertThat(doadora.getColetas()).doesNotContain(coletaBack);
        assertThat(coletaBack.getDoadora()).isNull();

        doadora.coletas(new HashSet<>(Set.of(coletaBack)));
        assertThat(doadora.getColetas()).containsOnly(coletaBack);
        assertThat(coletaBack.getDoadora()).isEqualTo(doadora);

        doadora.setColetas(new HashSet<>());
        assertThat(doadora.getColetas()).doesNotContain(coletaBack);
        assertThat(coletaBack.getDoadora()).isNull();
    }
}
