package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ColetaCriteriaTest {

    @Test
    void newColetaCriteriaHasAllFiltersNullTest() {
        var coletaCriteria = new ColetaCriteria();
        assertThat(coletaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void coletaCriteriaFluentMethodsCreatesFiltersTest() {
        var coletaCriteria = new ColetaCriteria();

        setAllFilters(coletaCriteria);

        assertThat(coletaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void coletaCriteriaCopyCreatesNullFilterTest() {
        var coletaCriteria = new ColetaCriteria();
        var copy = coletaCriteria.copy();

        assertThat(coletaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(coletaCriteria)
        );
    }

    @Test
    void coletaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var coletaCriteria = new ColetaCriteria();
        setAllFilters(coletaCriteria);

        var copy = coletaCriteria.copy();

        assertThat(coletaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(coletaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var coletaCriteria = new ColetaCriteria();

        assertThat(coletaCriteria).hasToString("ColetaCriteria{}");
    }

    private static void setAllFilters(ColetaCriteria coletaCriteria) {
        coletaCriteria.id();
        coletaCriteria.dataColeta();
        coletaCriteria.volumeMl();
        coletaCriteria.temperatura();
        coletaCriteria.localColeta();
        coletaCriteria.observacoes();
        coletaCriteria.statusColeta();
        coletaCriteria.processamentoId();
        coletaCriteria.doadoraId();
        coletaCriteria.distinct();
    }

    private static Condition<ColetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataColeta()) &&
                condition.apply(criteria.getVolumeMl()) &&
                condition.apply(criteria.getTemperatura()) &&
                condition.apply(criteria.getLocalColeta()) &&
                condition.apply(criteria.getObservacoes()) &&
                condition.apply(criteria.getStatusColeta()) &&
                condition.apply(criteria.getProcessamentoId()) &&
                condition.apply(criteria.getDoadoraId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ColetaCriteria> copyFiltersAre(ColetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataColeta(), copy.getDataColeta()) &&
                condition.apply(criteria.getVolumeMl(), copy.getVolumeMl()) &&
                condition.apply(criteria.getTemperatura(), copy.getTemperatura()) &&
                condition.apply(criteria.getLocalColeta(), copy.getLocalColeta()) &&
                condition.apply(criteria.getObservacoes(), copy.getObservacoes()) &&
                condition.apply(criteria.getStatusColeta(), copy.getStatusColeta()) &&
                condition.apply(criteria.getProcessamentoId(), copy.getProcessamentoId()) &&
                condition.apply(criteria.getDoadoraId(), copy.getDoadoraId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
