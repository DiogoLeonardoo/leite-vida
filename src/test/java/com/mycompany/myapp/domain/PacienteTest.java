package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DistribuicaoTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void distribuicoesTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Distribuicao distribuicaoBack = getDistribuicaoRandomSampleGenerator();

        paciente.addDistribuicoes(distribuicaoBack);
        assertThat(paciente.getDistribuicoes()).containsOnly(distribuicaoBack);
        assertThat(distribuicaoBack.getPaciente()).isEqualTo(paciente);

        paciente.removeDistribuicoes(distribuicaoBack);
        assertThat(paciente.getDistribuicoes()).doesNotContain(distribuicaoBack);
        assertThat(distribuicaoBack.getPaciente()).isNull();

        paciente.distribuicoes(new HashSet<>(Set.of(distribuicaoBack)));
        assertThat(paciente.getDistribuicoes()).containsOnly(distribuicaoBack);
        assertThat(distribuicaoBack.getPaciente()).isEqualTo(paciente);

        paciente.setDistribuicoes(new HashSet<>());
        assertThat(paciente.getDistribuicoes()).doesNotContain(distribuicaoBack);
        assertThat(distribuicaoBack.getPaciente()).isNull();
    }
}
