package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.ResultadoAnalise;
import com.mycompany.myapp.domain.enumeration.StatusProcessamento;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Processamento} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProcessamentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /processamentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessamentoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ResultadoAnalise
     */
    public static class ResultadoAnaliseFilter extends Filter<ResultadoAnalise> {

        public ResultadoAnaliseFilter() {}

        public ResultadoAnaliseFilter(ResultadoAnaliseFilter filter) {
            super(filter);
        }

        @Override
        public ResultadoAnaliseFilter copy() {
            return new ResultadoAnaliseFilter(this);
        }
    }

    /**
     * Class for filtering StatusProcessamento
     */
    public static class StatusProcessamentoFilter extends Filter<StatusProcessamento> {

        public StatusProcessamentoFilter() {}

        public StatusProcessamentoFilter(StatusProcessamentoFilter filter) {
            super(filter);
        }

        @Override
        public StatusProcessamentoFilter copy() {
            return new StatusProcessamentoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataProcessamento;

    private StringFilter tecnicoResponsavel;

    private DoubleFilter valorAcidezDornic;

    private DoubleFilter valorCaloricoKcal;

    private ResultadoAnaliseFilter resultadoAnalise;

    private StatusProcessamentoFilter statusProcessamento;

    private LongFilter estoqueId;

    private LongFilter coletaId;

    private Boolean distinct;

    public ProcessamentoCriteria() {}

    public ProcessamentoCriteria(ProcessamentoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataProcessamento = other.optionalDataProcessamento().map(LocalDateFilter::copy).orElse(null);
        this.tecnicoResponsavel = other.optionalTecnicoResponsavel().map(StringFilter::copy).orElse(null);
        this.valorAcidezDornic = other.optionalValorAcidezDornic().map(DoubleFilter::copy).orElse(null);
        this.valorCaloricoKcal = other.optionalValorCaloricoKcal().map(DoubleFilter::copy).orElse(null);
        this.resultadoAnalise = other.optionalResultadoAnalise().map(ResultadoAnaliseFilter::copy).orElse(null);
        this.statusProcessamento = other.optionalStatusProcessamento().map(StatusProcessamentoFilter::copy).orElse(null);
        this.estoqueId = other.optionalEstoqueId().map(LongFilter::copy).orElse(null);
        this.coletaId = other.optionalColetaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProcessamentoCriteria copy() {
        return new ProcessamentoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDataProcessamento() {
        return dataProcessamento;
    }

    public Optional<LocalDateFilter> optionalDataProcessamento() {
        return Optional.ofNullable(dataProcessamento);
    }

    public LocalDateFilter dataProcessamento() {
        if (dataProcessamento == null) {
            setDataProcessamento(new LocalDateFilter());
        }
        return dataProcessamento;
    }

    public void setDataProcessamento(LocalDateFilter dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public StringFilter getTecnicoResponsavel() {
        return tecnicoResponsavel;
    }

    public Optional<StringFilter> optionalTecnicoResponsavel() {
        return Optional.ofNullable(tecnicoResponsavel);
    }

    public StringFilter tecnicoResponsavel() {
        if (tecnicoResponsavel == null) {
            setTecnicoResponsavel(new StringFilter());
        }
        return tecnicoResponsavel;
    }

    public void setTecnicoResponsavel(StringFilter tecnicoResponsavel) {
        this.tecnicoResponsavel = tecnicoResponsavel;
    }

    public DoubleFilter getValorAcidezDornic() {
        return valorAcidezDornic;
    }

    public Optional<DoubleFilter> optionalValorAcidezDornic() {
        return Optional.ofNullable(valorAcidezDornic);
    }

    public DoubleFilter valorAcidezDornic() {
        if (valorAcidezDornic == null) {
            setValorAcidezDornic(new DoubleFilter());
        }
        return valorAcidezDornic;
    }

    public void setValorAcidezDornic(DoubleFilter valorAcidezDornic) {
        this.valorAcidezDornic = valorAcidezDornic;
    }

    public DoubleFilter getValorCaloricoKcal() {
        return valorCaloricoKcal;
    }

    public Optional<DoubleFilter> optionalValorCaloricoKcal() {
        return Optional.ofNullable(valorCaloricoKcal);
    }

    public DoubleFilter valorCaloricoKcal() {
        if (valorCaloricoKcal == null) {
            setValorCaloricoKcal(new DoubleFilter());
        }
        return valorCaloricoKcal;
    }

    public void setValorCaloricoKcal(DoubleFilter valorCaloricoKcal) {
        this.valorCaloricoKcal = valorCaloricoKcal;
    }

    public ResultadoAnaliseFilter getResultadoAnalise() {
        return resultadoAnalise;
    }

    public Optional<ResultadoAnaliseFilter> optionalResultadoAnalise() {
        return Optional.ofNullable(resultadoAnalise);
    }

    public ResultadoAnaliseFilter resultadoAnalise() {
        if (resultadoAnalise == null) {
            setResultadoAnalise(new ResultadoAnaliseFilter());
        }
        return resultadoAnalise;
    }

    public void setResultadoAnalise(ResultadoAnaliseFilter resultadoAnalise) {
        this.resultadoAnalise = resultadoAnalise;
    }

    public StatusProcessamentoFilter getStatusProcessamento() {
        return statusProcessamento;
    }

    public Optional<StatusProcessamentoFilter> optionalStatusProcessamento() {
        return Optional.ofNullable(statusProcessamento);
    }

    public StatusProcessamentoFilter statusProcessamento() {
        if (statusProcessamento == null) {
            setStatusProcessamento(new StatusProcessamentoFilter());
        }
        return statusProcessamento;
    }

    public void setStatusProcessamento(StatusProcessamentoFilter statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public LongFilter getEstoqueId() {
        return estoqueId;
    }

    public Optional<LongFilter> optionalEstoqueId() {
        return Optional.ofNullable(estoqueId);
    }

    public LongFilter estoqueId() {
        if (estoqueId == null) {
            setEstoqueId(new LongFilter());
        }
        return estoqueId;
    }

    public void setEstoqueId(LongFilter estoqueId) {
        this.estoqueId = estoqueId;
    }

    public LongFilter getColetaId() {
        return coletaId;
    }

    public Optional<LongFilter> optionalColetaId() {
        return Optional.ofNullable(coletaId);
    }

    public LongFilter coletaId() {
        if (coletaId == null) {
            setColetaId(new LongFilter());
        }
        return coletaId;
    }

    public void setColetaId(LongFilter coletaId) {
        this.coletaId = coletaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProcessamentoCriteria that = (ProcessamentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataProcessamento, that.dataProcessamento) &&
            Objects.equals(tecnicoResponsavel, that.tecnicoResponsavel) &&
            Objects.equals(valorAcidezDornic, that.valorAcidezDornic) &&
            Objects.equals(valorCaloricoKcal, that.valorCaloricoKcal) &&
            Objects.equals(resultadoAnalise, that.resultadoAnalise) &&
            Objects.equals(statusProcessamento, that.statusProcessamento) &&
            Objects.equals(estoqueId, that.estoqueId) &&
            Objects.equals(coletaId, that.coletaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataProcessamento,
            tecnicoResponsavel,
            valorAcidezDornic,
            valorCaloricoKcal,
            resultadoAnalise,
            statusProcessamento,
            estoqueId,
            coletaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessamentoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataProcessamento().map(f -> "dataProcessamento=" + f + ", ").orElse("") +
            optionalTecnicoResponsavel().map(f -> "tecnicoResponsavel=" + f + ", ").orElse("") +
            optionalValorAcidezDornic().map(f -> "valorAcidezDornic=" + f + ", ").orElse("") +
            optionalValorCaloricoKcal().map(f -> "valorCaloricoKcal=" + f + ", ").orElse("") +
            optionalResultadoAnalise().map(f -> "resultadoAnalise=" + f + ", ").orElse("") +
            optionalStatusProcessamento().map(f -> "statusProcessamento=" + f + ", ").orElse("") +
            optionalEstoqueId().map(f -> "estoqueId=" + f + ", ").orElse("") +
            optionalColetaId().map(f -> "coletaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
