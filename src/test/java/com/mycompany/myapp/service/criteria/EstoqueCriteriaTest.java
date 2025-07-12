package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EstoqueCriteriaTest {

    @Test
    void newEstoqueCriteriaHasAllFiltersNullTest() {
        var estoqueCriteria = new EstoqueCriteria();
        assertThat(estoqueCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void estoqueCriteriaFluentMethodsCreatesFiltersTest() {
        var estoqueCriteria = new EstoqueCriteria();

        setAllFilters(estoqueCriteria);

        assertThat(estoqueCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void estoqueCriteriaCopyCreatesNullFilterTest() {
        var estoqueCriteria = new EstoqueCriteria();
        var copy = estoqueCriteria.copy();

        assertThat(estoqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(estoqueCriteria)
        );
    }

    @Test
    void estoqueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var estoqueCriteria = new EstoqueCriteria();
        setAllFilters(estoqueCriteria);

        var copy = estoqueCriteria.copy();

        assertThat(estoqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(estoqueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var estoqueCriteria = new EstoqueCriteria();

        assertThat(estoqueCriteria).hasToString("EstoqueCriteria{}");
    }

    private static void setAllFilters(EstoqueCriteria estoqueCriteria) {
        estoqueCriteria.id();
        estoqueCriteria.dataProducao();
        estoqueCriteria.dataValidade();
        estoqueCriteria.tipoLeite();
        estoqueCriteria.classificacao();
        estoqueCriteria.volumeTotalMl();
        estoqueCriteria.volumeDisponivelMl();
        estoqueCriteria.localArmazenamento();
        estoqueCriteria.temperaturaArmazenamento();
        estoqueCriteria.statusLote();
        estoqueCriteria.distribuicoesId();
        estoqueCriteria.processamentoId();
        estoqueCriteria.distinct();
    }

    private static Condition<EstoqueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataProducao()) &&
                condition.apply(criteria.getDataValidade()) &&
                condition.apply(criteria.getTipoLeite()) &&
                condition.apply(criteria.getClassificacao()) &&
                condition.apply(criteria.getVolumeTotalMl()) &&
                condition.apply(criteria.getVolumeDisponivelMl()) &&
                condition.apply(criteria.getLocalArmazenamento()) &&
                condition.apply(criteria.getTemperaturaArmazenamento()) &&
                condition.apply(criteria.getStatusLote()) &&
                condition.apply(criteria.getDistribuicoesId()) &&
                condition.apply(criteria.getProcessamentoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EstoqueCriteria> copyFiltersAre(EstoqueCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataProducao(), copy.getDataProducao()) &&
                condition.apply(criteria.getDataValidade(), copy.getDataValidade()) &&
                condition.apply(criteria.getTipoLeite(), copy.getTipoLeite()) &&
                condition.apply(criteria.getClassificacao(), copy.getClassificacao()) &&
                condition.apply(criteria.getVolumeTotalMl(), copy.getVolumeTotalMl()) &&
                condition.apply(criteria.getVolumeDisponivelMl(), copy.getVolumeDisponivelMl()) &&
                condition.apply(criteria.getLocalArmazenamento(), copy.getLocalArmazenamento()) &&
                condition.apply(criteria.getTemperaturaArmazenamento(), copy.getTemperaturaArmazenamento()) &&
                condition.apply(criteria.getStatusLote(), copy.getStatusLote()) &&
                condition.apply(criteria.getDistribuicoesId(), copy.getDistribuicoesId()) &&
                condition.apply(criteria.getProcessamentoId(), copy.getProcessamentoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
