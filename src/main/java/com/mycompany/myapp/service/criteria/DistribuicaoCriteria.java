package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Distribuicao} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DistribuicaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /distribuicaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DistribuicaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataDistribuicao;

    private DoubleFilter volumeDistribuidoMl;

    private StringFilter responsavelEntrega;

    private StringFilter responsavelRecebimento;

    private StringFilter observacoes;

    private LongFilter estoqueId;

    private LongFilter pacienteId;

    private Boolean distinct;

    public DistribuicaoCriteria() {}

    public DistribuicaoCriteria(DistribuicaoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataDistribuicao = other.optionalDataDistribuicao().map(LocalDateFilter::copy).orElse(null);
        this.volumeDistribuidoMl = other.optionalVolumeDistribuidoMl().map(DoubleFilter::copy).orElse(null);
        this.responsavelEntrega = other.optionalResponsavelEntrega().map(StringFilter::copy).orElse(null);
        this.responsavelRecebimento = other.optionalResponsavelRecebimento().map(StringFilter::copy).orElse(null);
        this.observacoes = other.optionalObservacoes().map(StringFilter::copy).orElse(null);
        this.estoqueId = other.optionalEstoqueId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DistribuicaoCriteria copy() {
        return new DistribuicaoCriteria(this);
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

    public LocalDateFilter getDataDistribuicao() {
        return dataDistribuicao;
    }

    public Optional<LocalDateFilter> optionalDataDistribuicao() {
        return Optional.ofNullable(dataDistribuicao);
    }

    public LocalDateFilter dataDistribuicao() {
        if (dataDistribuicao == null) {
            setDataDistribuicao(new LocalDateFilter());
        }
        return dataDistribuicao;
    }

    public void setDataDistribuicao(LocalDateFilter dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }

    public DoubleFilter getVolumeDistribuidoMl() {
        return volumeDistribuidoMl;
    }

    public Optional<DoubleFilter> optionalVolumeDistribuidoMl() {
        return Optional.ofNullable(volumeDistribuidoMl);
    }

    public DoubleFilter volumeDistribuidoMl() {
        if (volumeDistribuidoMl == null) {
            setVolumeDistribuidoMl(new DoubleFilter());
        }
        return volumeDistribuidoMl;
    }

    public void setVolumeDistribuidoMl(DoubleFilter volumeDistribuidoMl) {
        this.volumeDistribuidoMl = volumeDistribuidoMl;
    }

    public StringFilter getResponsavelEntrega() {
        return responsavelEntrega;
    }

    public Optional<StringFilter> optionalResponsavelEntrega() {
        return Optional.ofNullable(responsavelEntrega);
    }

    public StringFilter responsavelEntrega() {
        if (responsavelEntrega == null) {
            setResponsavelEntrega(new StringFilter());
        }
        return responsavelEntrega;
    }

    public void setResponsavelEntrega(StringFilter responsavelEntrega) {
        this.responsavelEntrega = responsavelEntrega;
    }

    public StringFilter getResponsavelRecebimento() {
        return responsavelRecebimento;
    }

    public Optional<StringFilter> optionalResponsavelRecebimento() {
        return Optional.ofNullable(responsavelRecebimento);
    }

    public StringFilter responsavelRecebimento() {
        if (responsavelRecebimento == null) {
            setResponsavelRecebimento(new StringFilter());
        }
        return responsavelRecebimento;
    }

    public void setResponsavelRecebimento(StringFilter responsavelRecebimento) {
        this.responsavelRecebimento = responsavelRecebimento;
    }

    public StringFilter getObservacoes() {
        return observacoes;
    }

    public Optional<StringFilter> optionalObservacoes() {
        return Optional.ofNullable(observacoes);
    }

    public StringFilter observacoes() {
        if (observacoes == null) {
            setObservacoes(new StringFilter());
        }
        return observacoes;
    }

    public void setObservacoes(StringFilter observacoes) {
        this.observacoes = observacoes;
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

    public LongFilter getPacienteId() {
        return pacienteId;
    }

    public Optional<LongFilter> optionalPacienteId() {
        return Optional.ofNullable(pacienteId);
    }

    public LongFilter pacienteId() {
        if (pacienteId == null) {
            setPacienteId(new LongFilter());
        }
        return pacienteId;
    }

    public void setPacienteId(LongFilter pacienteId) {
        this.pacienteId = pacienteId;
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
        final DistribuicaoCriteria that = (DistribuicaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataDistribuicao, that.dataDistribuicao) &&
            Objects.equals(volumeDistribuidoMl, that.volumeDistribuidoMl) &&
            Objects.equals(responsavelEntrega, that.responsavelEntrega) &&
            Objects.equals(responsavelRecebimento, that.responsavelRecebimento) &&
            Objects.equals(observacoes, that.observacoes) &&
            Objects.equals(estoqueId, that.estoqueId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataDistribuicao,
            volumeDistribuidoMl,
            responsavelEntrega,
            responsavelRecebimento,
            observacoes,
            estoqueId,
            pacienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistribuicaoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataDistribuicao().map(f -> "dataDistribuicao=" + f + ", ").orElse("") +
            optionalVolumeDistribuidoMl().map(f -> "volumeDistribuidoMl=" + f + ", ").orElse("") +
            optionalResponsavelEntrega().map(f -> "responsavelEntrega=" + f + ", ").orElse("") +
            optionalResponsavelRecebimento().map(f -> "responsavelRecebimento=" + f + ", ").orElse("") +
            optionalObservacoes().map(f -> "observacoes=" + f + ", ").orElse("") +
            optionalEstoqueId().map(f -> "estoqueId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
