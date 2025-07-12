package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DistribuicaoTestSamples.*;
import static com.mycompany.myapp.domain.EstoqueTestSamples.*;
import static com.mycompany.myapp.domain.ProcessamentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EstoqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estoque.class);
        Estoque estoque1 = getEstoqueSample1();
        Estoque estoque2 = new Estoque();
        assertThat(estoque1).isNotEqualTo(estoque2);

        estoque2.setId(estoque1.getId());
        assertThat(estoque1).isEqualTo(estoque2);

        estoque2 = getEstoqueSample2();
        assertThat(estoque1).isNotEqualTo(estoque2);
    }

    @Test
    void distribuicoesTest() {
        Estoque estoque = getEstoqueRandomSampleGenerator();
        Distribuicao distribuicaoBack = getDistribuicaoRandomSampleGenerator();

        estoque.addDistribuicoes(distribuicaoBack);
        assertThat(estoque.getDistribuicoes()).containsOnly(distribuicaoBack);
        assertThat(distribuicaoBack.getEstoque()).isEqualTo(estoque);

        estoque.removeDistribuicoes(distribuicaoBack);
        assertThat(estoque.getDistribuicoes()).doesNotContain(distribuicaoBack);
        assertThat(distribuicaoBack.getEstoque()).isNull();

        estoque.distribuicoes(new HashSet<>(Set.of(distribuicaoBack)));
        assertThat(estoque.getDistribuicoes()).containsOnly(distribuicaoBack);
        assertThat(distribuicaoBack.getEstoque()).isEqualTo(estoque);

        estoque.setDistribuicoes(new HashSet<>());
        assertThat(estoque.getDistribuicoes()).doesNotContain(distribuicaoBack);
        assertThat(distribuicaoBack.getEstoque()).isNull();
    }

    @Test
    void processamentoTest() {
        Estoque estoque = getEstoqueRandomSampleGenerator();
        Processamento processamentoBack = getProcessamentoRandomSampleGenerator();

        estoque.setProcessamento(processamentoBack);
        assertThat(estoque.getProcessamento()).isEqualTo(processamentoBack);
        assertThat(processamentoBack.getEstoque()).isEqualTo(estoque);

        estoque.processamento(null);
        assertThat(estoque.getProcessamento()).isNull();
        assertThat(processamentoBack.getEstoque()).isNull();
    }
}
