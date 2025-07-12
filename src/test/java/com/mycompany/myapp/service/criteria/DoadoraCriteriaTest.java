package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DoadoraCriteriaTest {

    @Test
    void newDoadoraCriteriaHasAllFiltersNullTest() {
        var doadoraCriteria = new DoadoraCriteria();
        assertThat(doadoraCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void doadoraCriteriaFluentMethodsCreatesFiltersTest() {
        var doadoraCriteria = new DoadoraCriteria();

        setAllFilters(doadoraCriteria);

        assertThat(doadoraCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void doadoraCriteriaCopyCreatesNullFilterTest() {
        var doadoraCriteria = new DoadoraCriteria();
        var copy = doadoraCriteria.copy();

        assertThat(doadoraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(doadoraCriteria)
        );
    }

    @Test
    void doadoraCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var doadoraCriteria = new DoadoraCriteria();
        setAllFilters(doadoraCriteria);

        var copy = doadoraCriteria.copy();

        assertThat(doadoraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(doadoraCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var doadoraCriteria = new DoadoraCriteria();

        assertThat(doadoraCriteria).hasToString("DoadoraCriteria{}");
    }

    private static void setAllFilters(DoadoraCriteria doadoraCriteria) {
        doadoraCriteria.id();
        doadoraCriteria.nome();
        doadoraCriteria.cartaoSUS();
        doadoraCriteria.cpf();
        doadoraCriteria.dataNascimento();
        doadoraCriteria.cep();
        doadoraCriteria.estado();
        doadoraCriteria.cidade();
        doadoraCriteria.endereco();
        doadoraCriteria.telefone();
        doadoraCriteria.profissao();
        doadoraCriteria.tipoDoadora();
        doadoraCriteria.localPreNatal();
        doadoraCriteria.resultadoVDRL();
        doadoraCriteria.resultadoHBsAg();
        doadoraCriteria.resultadoFTAabs();
        doadoraCriteria.resultadoHIV();
        doadoraCriteria.transfusaoUltimos5Anos();
        doadoraCriteria.dataRegistro();
        doadoraCriteria.coletasId();
        doadoraCriteria.distinct();
    }

    private static Condition<DoadoraCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getCartaoSUS()) &&
                condition.apply(criteria.getCpf()) &&
                condition.apply(criteria.getDataNascimento()) &&
                condition.apply(criteria.getCep()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getCidade()) &&
                condition.apply(criteria.getEndereco()) &&
                condition.apply(criteria.getTelefone()) &&
                condition.apply(criteria.getProfissao()) &&
                condition.apply(criteria.getTipoDoadora()) &&
                condition.apply(criteria.getLocalPreNatal()) &&
                condition.apply(criteria.getResultadoVDRL()) &&
                condition.apply(criteria.getResultadoHBsAg()) &&
                condition.apply(criteria.getResultadoFTAabs()) &&
                condition.apply(criteria.getResultadoHIV()) &&
                condition.apply(criteria.getTransfusaoUltimos5Anos()) &&
                condition.apply(criteria.getDataRegistro()) &&
                condition.apply(criteria.getColetasId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DoadoraCriteria> copyFiltersAre(DoadoraCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getCartaoSUS(), copy.getCartaoSUS()) &&
                condition.apply(criteria.getCpf(), copy.getCpf()) &&
                condition.apply(criteria.getDataNascimento(), copy.getDataNascimento()) &&
                condition.apply(criteria.getCep(), copy.getCep()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getCidade(), copy.getCidade()) &&
                condition.apply(criteria.getEndereco(), copy.getEndereco()) &&
                condition.apply(criteria.getTelefone(), copy.getTelefone()) &&
                condition.apply(criteria.getProfissao(), copy.getProfissao()) &&
                condition.apply(criteria.getTipoDoadora(), copy.getTipoDoadora()) &&
                condition.apply(criteria.getLocalPreNatal(), copy.getLocalPreNatal()) &&
                condition.apply(criteria.getResultadoVDRL(), copy.getResultadoVDRL()) &&
                condition.apply(criteria.getResultadoHBsAg(), copy.getResultadoHBsAg()) &&
                condition.apply(criteria.getResultadoFTAabs(), copy.getResultadoFTAabs()) &&
                condition.apply(criteria.getResultadoHIV(), copy.getResultadoHIV()) &&
                condition.apply(criteria.getTransfusaoUltimos5Anos(), copy.getTransfusaoUltimos5Anos()) &&
                condition.apply(criteria.getDataRegistro(), copy.getDataRegistro()) &&
                condition.apply(criteria.getColetasId(), copy.getColetasId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
