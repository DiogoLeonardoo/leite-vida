package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PacienteCriteriaTest {

    @Test
    void newPacienteCriteriaHasAllFiltersNullTest() {
        var pacienteCriteria = new PacienteCriteria();
        assertThat(pacienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pacienteCriteriaFluentMethodsCreatesFiltersTest() {
        var pacienteCriteria = new PacienteCriteria();

        setAllFilters(pacienteCriteria);

        assertThat(pacienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pacienteCriteriaCopyCreatesNullFilterTest() {
        var pacienteCriteria = new PacienteCriteria();
        var copy = pacienteCriteria.copy();

        assertThat(pacienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pacienteCriteria)
        );
    }

    @Test
    void pacienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pacienteCriteria = new PacienteCriteria();
        setAllFilters(pacienteCriteria);

        var copy = pacienteCriteria.copy();

        assertThat(pacienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pacienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pacienteCriteria = new PacienteCriteria();

        assertThat(pacienteCriteria).hasToString("PacienteCriteria{}");
    }

    private static void setAllFilters(PacienteCriteria pacienteCriteria) {
        pacienteCriteria.id();
        pacienteCriteria.nome();
        pacienteCriteria.registroHospitalar();
        pacienteCriteria.dataNascimento();
        pacienteCriteria.pesoNascimento();
        pacienteCriteria.idadeGestacional();
        pacienteCriteria.condicaoClinica();
        pacienteCriteria.nomeResponsavel();
        pacienteCriteria.cpfResponsavel();
        pacienteCriteria.telefoneResponsavel();
        pacienteCriteria.parentescoResponsavel();
        pacienteCriteria.statusAtivo();
        pacienteCriteria.distribuicoesId();
        pacienteCriteria.distinct();
    }

    private static Condition<PacienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getRegistroHospitalar()) &&
                condition.apply(criteria.getDataNascimento()) &&
                condition.apply(criteria.getPesoNascimento()) &&
                condition.apply(criteria.getIdadeGestacional()) &&
                condition.apply(criteria.getCondicaoClinica()) &&
                condition.apply(criteria.getNomeResponsavel()) &&
                condition.apply(criteria.getCpfResponsavel()) &&
                condition.apply(criteria.getTelefoneResponsavel()) &&
                condition.apply(criteria.getParentescoResponsavel()) &&
                condition.apply(criteria.getStatusAtivo()) &&
                condition.apply(criteria.getDistribuicoesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PacienteCriteria> copyFiltersAre(PacienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getRegistroHospitalar(), copy.getRegistroHospitalar()) &&
                condition.apply(criteria.getDataNascimento(), copy.getDataNascimento()) &&
                condition.apply(criteria.getPesoNascimento(), copy.getPesoNascimento()) &&
                condition.apply(criteria.getIdadeGestacional(), copy.getIdadeGestacional()) &&
                condition.apply(criteria.getCondicaoClinica(), copy.getCondicaoClinica()) &&
                condition.apply(criteria.getNomeResponsavel(), copy.getNomeResponsavel()) &&
                condition.apply(criteria.getCpfResponsavel(), copy.getCpfResponsavel()) &&
                condition.apply(criteria.getTelefoneResponsavel(), copy.getTelefoneResponsavel()) &&
                condition.apply(criteria.getParentescoResponsavel(), copy.getParentescoResponsavel()) &&
                condition.apply(criteria.getStatusAtivo(), copy.getStatusAtivo()) &&
                condition.apply(criteria.getDistribuicoesId(), copy.getDistribuicoesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
