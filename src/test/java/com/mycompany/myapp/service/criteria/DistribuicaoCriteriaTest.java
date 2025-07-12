package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DistribuicaoCriteriaTest {

    @Test
    void newDistribuicaoCriteriaHasAllFiltersNullTest() {
        var distribuicaoCriteria = new DistribuicaoCriteria();
        assertThat(distribuicaoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void distribuicaoCriteriaFluentMethodsCreatesFiltersTest() {
        var distribuicaoCriteria = new DistribuicaoCriteria();

        setAllFilters(distribuicaoCriteria);

        assertThat(distribuicaoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void distribuicaoCriteriaCopyCreatesNullFilterTest() {
        var distribuicaoCriteria = new DistribuicaoCriteria();
        var copy = distribuicaoCriteria.copy();

        assertThat(distribuicaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(distribuicaoCriteria)
        );
    }

    @Test
    void distribuicaoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var distribuicaoCriteria = new DistribuicaoCriteria();
        setAllFilters(distribuicaoCriteria);

        var copy = distribuicaoCriteria.copy();

        assertThat(distribuicaoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(distribuicaoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var distribuicaoCriteria = new DistribuicaoCriteria();

        assertThat(distribuicaoCriteria).hasToString("DistribuicaoCriteria{}");
    }

    private static void setAllFilters(DistribuicaoCriteria distribuicaoCriteria) {
        distribuicaoCriteria.id();
        distribuicaoCriteria.dataDistribuicao();
        distribuicaoCriteria.volumeDistribuidoMl();
        distribuicaoCriteria.responsavelEntrega();
        distribuicaoCriteria.responsavelRecebimento();
        distribuicaoCriteria.observacoes();
        distribuicaoCriteria.estoqueId();
        distribuicaoCriteria.pacienteId();
        distribuicaoCriteria.distinct();
    }

    private static Condition<DistribuicaoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataDistribuicao()) &&
                condition.apply(criteria.getVolumeDistribuidoMl()) &&
                condition.apply(criteria.getResponsavelEntrega()) &&
                condition.apply(criteria.getResponsavelRecebimento()) &&
                condition.apply(criteria.getObservacoes()) &&
                condition.apply(criteria.getEstoqueId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DistribuicaoCriteria> copyFiltersAre(
        DistribuicaoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataDistribuicao(), copy.getDataDistribuicao()) &&
                condition.apply(criteria.getVolumeDistribuidoMl(), copy.getVolumeDistribuidoMl()) &&
                condition.apply(criteria.getResponsavelEntrega(), copy.getResponsavelEntrega()) &&
                condition.apply(criteria.getResponsavelRecebimento(), copy.getResponsavelRecebimento()) &&
                condition.apply(criteria.getObservacoes(), copy.getObservacoes()) &&
                condition.apply(criteria.getEstoqueId(), copy.getEstoqueId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
