package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProcessamentoCriteriaTest {

    @Test
    void newProcessamentoCriteriaHasAllFiltersNullTest() {
        var processamentoCriteria = new ProcessamentoCriteria();
        assertThat(processamentoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void processamentoCriteriaFluentMethodsCreatesFiltersTest() {
        var processamentoCriteria = new ProcessamentoCriteria();

        setAllFilters(processamentoCriteria);

        assertThat(processamentoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void processamentoCriteriaCopyCreatesNullFilterTest() {
        var processamentoCriteria = new ProcessamentoCriteria();
        var copy = processamentoCriteria.copy();

        assertThat(processamentoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(processamentoCriteria)
        );
    }

    @Test
    void processamentoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var processamentoCriteria = new ProcessamentoCriteria();
        setAllFilters(processamentoCriteria);

        var copy = processamentoCriteria.copy();

        assertThat(processamentoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(processamentoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var processamentoCriteria = new ProcessamentoCriteria();

        assertThat(processamentoCriteria).hasToString("ProcessamentoCriteria{}");
    }

    private static void setAllFilters(ProcessamentoCriteria processamentoCriteria) {
        processamentoCriteria.id();
        processamentoCriteria.dataProcessamento();
        processamentoCriteria.tecnicoResponsavel();
        processamentoCriteria.valorAcidezDornic();
        processamentoCriteria.valorCaloricoKcal();
        processamentoCriteria.resultadoAnalise();
        processamentoCriteria.statusProcessamento();
        processamentoCriteria.estoqueId();
        processamentoCriteria.coletaId();
        processamentoCriteria.distinct();
    }

    private static Condition<ProcessamentoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataProcessamento()) &&
                condition.apply(criteria.getTecnicoResponsavel()) &&
                condition.apply(criteria.getValorAcidezDornic()) &&
                condition.apply(criteria.getValorCaloricoKcal()) &&
                condition.apply(criteria.getResultadoAnalise()) &&
                condition.apply(criteria.getStatusProcessamento()) &&
                condition.apply(criteria.getEstoqueId()) &&
                condition.apply(criteria.getColetaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProcessamentoCriteria> copyFiltersAre(
        ProcessamentoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataProcessamento(), copy.getDataProcessamento()) &&
                condition.apply(criteria.getTecnicoResponsavel(), copy.getTecnicoResponsavel()) &&
                condition.apply(criteria.getValorAcidezDornic(), copy.getValorAcidezDornic()) &&
                condition.apply(criteria.getValorCaloricoKcal(), copy.getValorCaloricoKcal()) &&
                condition.apply(criteria.getResultadoAnalise(), copy.getResultadoAnalise()) &&
                condition.apply(criteria.getStatusProcessamento(), copy.getStatusProcessamento()) &&
                condition.apply(criteria.getEstoqueId(), copy.getEstoqueId()) &&
                condition.apply(criteria.getColetaId(), copy.getColetaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
