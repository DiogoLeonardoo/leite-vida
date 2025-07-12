package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.StatusColeta;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Coleta} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ColetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /coletas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ColetaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusColeta
     */
    public static class StatusColetaFilter extends Filter<StatusColeta> {

        public StatusColetaFilter() {}

        public StatusColetaFilter(StatusColetaFilter filter) {
            super(filter);
        }

        @Override
        public StatusColetaFilter copy() {
            return new StatusColetaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataColeta;

    private DoubleFilter volumeMl;

    private DoubleFilter temperatura;

    private StringFilter localColeta;

    private StringFilter observacoes;

    private StatusColetaFilter statusColeta;

    private LongFilter processamentoId;

    private LongFilter doadoraId;

    private Boolean distinct;

    public ColetaCriteria() {}

    public ColetaCriteria(ColetaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataColeta = other.optionalDataColeta().map(LocalDateFilter::copy).orElse(null);
        this.volumeMl = other.optionalVolumeMl().map(DoubleFilter::copy).orElse(null);
        this.temperatura = other.optionalTemperatura().map(DoubleFilter::copy).orElse(null);
        this.localColeta = other.optionalLocalColeta().map(StringFilter::copy).orElse(null);
        this.observacoes = other.optionalObservacoes().map(StringFilter::copy).orElse(null);
        this.statusColeta = other.optionalStatusColeta().map(StatusColetaFilter::copy).orElse(null);
        this.processamentoId = other.optionalProcessamentoId().map(LongFilter::copy).orElse(null);
        this.doadoraId = other.optionalDoadoraId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ColetaCriteria copy() {
        return new ColetaCriteria(this);
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

    public LocalDateFilter getDataColeta() {
        return dataColeta;
    }

    public Optional<LocalDateFilter> optionalDataColeta() {
        return Optional.ofNullable(dataColeta);
    }

    public LocalDateFilter dataColeta() {
        if (dataColeta == null) {
            setDataColeta(new LocalDateFilter());
        }
        return dataColeta;
    }

    public void setDataColeta(LocalDateFilter dataColeta) {
        this.dataColeta = dataColeta;
    }

    public DoubleFilter getVolumeMl() {
        return volumeMl;
    }

    public Optional<DoubleFilter> optionalVolumeMl() {
        return Optional.ofNullable(volumeMl);
    }

    public DoubleFilter volumeMl() {
        if (volumeMl == null) {
            setVolumeMl(new DoubleFilter());
        }
        return volumeMl;
    }

    public void setVolumeMl(DoubleFilter volumeMl) {
        this.volumeMl = volumeMl;
    }

    public DoubleFilter getTemperatura() {
        return temperatura;
    }

    public Optional<DoubleFilter> optionalTemperatura() {
        return Optional.ofNullable(temperatura);
    }

    public DoubleFilter temperatura() {
        if (temperatura == null) {
            setTemperatura(new DoubleFilter());
        }
        return temperatura;
    }

    public void setTemperatura(DoubleFilter temperatura) {
        this.temperatura = temperatura;
    }

    public StringFilter getLocalColeta() {
        return localColeta;
    }

    public Optional<StringFilter> optionalLocalColeta() {
        return Optional.ofNullable(localColeta);
    }

    public StringFilter localColeta() {
        if (localColeta == null) {
            setLocalColeta(new StringFilter());
        }
        return localColeta;
    }

    public void setLocalColeta(StringFilter localColeta) {
        this.localColeta = localColeta;
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

    public StatusColetaFilter getStatusColeta() {
        return statusColeta;
    }

    public Optional<StatusColetaFilter> optionalStatusColeta() {
        return Optional.ofNullable(statusColeta);
    }

    public StatusColetaFilter statusColeta() {
        if (statusColeta == null) {
            setStatusColeta(new StatusColetaFilter());
        }
        return statusColeta;
    }

    public void setStatusColeta(StatusColetaFilter statusColeta) {
        this.statusColeta = statusColeta;
    }

    public LongFilter getProcessamentoId() {
        return processamentoId;
    }

    public Optional<LongFilter> optionalProcessamentoId() {
        return Optional.ofNullable(processamentoId);
    }

    public LongFilter processamentoId() {
        if (processamentoId == null) {
            setProcessamentoId(new LongFilter());
        }
        return processamentoId;
    }

    public void setProcessamentoId(LongFilter processamentoId) {
        this.processamentoId = processamentoId;
    }

    public LongFilter getDoadoraId() {
        return doadoraId;
    }

    public Optional<LongFilter> optionalDoadoraId() {
        return Optional.ofNullable(doadoraId);
    }

    public LongFilter doadoraId() {
        if (doadoraId == null) {
            setDoadoraId(new LongFilter());
        }
        return doadoraId;
    }

    public void setDoadoraId(LongFilter doadoraId) {
        this.doadoraId = doadoraId;
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
        final ColetaCriteria that = (ColetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataColeta, that.dataColeta) &&
            Objects.equals(volumeMl, that.volumeMl) &&
            Objects.equals(temperatura, that.temperatura) &&
            Objects.equals(localColeta, that.localColeta) &&
            Objects.equals(observacoes, that.observacoes) &&
            Objects.equals(statusColeta, that.statusColeta) &&
            Objects.equals(processamentoId, that.processamentoId) &&
            Objects.equals(doadoraId, that.doadoraId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataColeta,
            volumeMl,
            temperatura,
            localColeta,
            observacoes,
            statusColeta,
            processamentoId,
            doadoraId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ColetaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataColeta().map(f -> "dataColeta=" + f + ", ").orElse("") +
            optionalVolumeMl().map(f -> "volumeMl=" + f + ", ").orElse("") +
            optionalTemperatura().map(f -> "temperatura=" + f + ", ").orElse("") +
            optionalLocalColeta().map(f -> "localColeta=" + f + ", ").orElse("") +
            optionalObservacoes().map(f -> "observacoes=" + f + ", ").orElse("") +
            optionalStatusColeta().map(f -> "statusColeta=" + f + ", ").orElse("") +
            optionalProcessamentoId().map(f -> "processamentoId=" + f + ", ").orElse("") +
            optionalDoadoraId().map(f -> "doadoraId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
