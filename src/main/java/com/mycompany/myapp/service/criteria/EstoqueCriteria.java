package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Estoque} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EstoqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /estoques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstoqueCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoLeite
     */
    public static class TipoLeiteFilter extends Filter<TipoLeite> {

        public TipoLeiteFilter() {}

        public TipoLeiteFilter(TipoLeiteFilter filter) {
            super(filter);
        }

        @Override
        public TipoLeiteFilter copy() {
            return new TipoLeiteFilter(this);
        }
    }

    /**
     * Class for filtering ClassificacaoLeite
     */
    public static class ClassificacaoLeiteFilter extends Filter<ClassificacaoLeite> {

        public ClassificacaoLeiteFilter() {}

        public ClassificacaoLeiteFilter(ClassificacaoLeiteFilter filter) {
            super(filter);
        }

        @Override
        public ClassificacaoLeiteFilter copy() {
            return new ClassificacaoLeiteFilter(this);
        }
    }

    /**
     * Class for filtering StatusLote
     */
    public static class StatusLoteFilter extends Filter<StatusLote> {

        public StatusLoteFilter() {}

        public StatusLoteFilter(StatusLoteFilter filter) {
            super(filter);
        }

        @Override
        public StatusLoteFilter copy() {
            return new StatusLoteFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataProducao;

    private LocalDateFilter dataValidade;

    private TipoLeiteFilter tipoLeite;

    private ClassificacaoLeiteFilter classificacao;

    private DoubleFilter volumeTotalMl;

    private DoubleFilter volumeDisponivelMl;

    private StringFilter localArmazenamento;

    private DoubleFilter temperaturaArmazenamento;

    private StatusLoteFilter statusLote;

    private LongFilter distribuicoesId;

    private LongFilter processamentoId;

    private Boolean distinct;

    public EstoqueCriteria() {}

    public EstoqueCriteria(EstoqueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataProducao = other.optionalDataProducao().map(LocalDateFilter::copy).orElse(null);
        this.dataValidade = other.optionalDataValidade().map(LocalDateFilter::copy).orElse(null);
        this.tipoLeite = other.optionalTipoLeite().map(TipoLeiteFilter::copy).orElse(null);
        this.classificacao = other.optionalClassificacao().map(ClassificacaoLeiteFilter::copy).orElse(null);
        this.volumeTotalMl = other.optionalVolumeTotalMl().map(DoubleFilter::copy).orElse(null);
        this.volumeDisponivelMl = other.optionalVolumeDisponivelMl().map(DoubleFilter::copy).orElse(null);
        this.localArmazenamento = other.optionalLocalArmazenamento().map(StringFilter::copy).orElse(null);
        this.temperaturaArmazenamento = other.optionalTemperaturaArmazenamento().map(DoubleFilter::copy).orElse(null);
        this.statusLote = other.optionalStatusLote().map(StatusLoteFilter::copy).orElse(null);
        this.distribuicoesId = other.optionalDistribuicoesId().map(LongFilter::copy).orElse(null);
        this.processamentoId = other.optionalProcessamentoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EstoqueCriteria copy() {
        return new EstoqueCriteria(this);
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

    public LocalDateFilter getDataProducao() {
        return dataProducao;
    }

    public Optional<LocalDateFilter> optionalDataProducao() {
        return Optional.ofNullable(dataProducao);
    }

    public LocalDateFilter dataProducao() {
        if (dataProducao == null) {
            setDataProducao(new LocalDateFilter());
        }
        return dataProducao;
    }

    public void setDataProducao(LocalDateFilter dataProducao) {
        this.dataProducao = dataProducao;
    }

    public LocalDateFilter getDataValidade() {
        return dataValidade;
    }

    public Optional<LocalDateFilter> optionalDataValidade() {
        return Optional.ofNullable(dataValidade);
    }

    public LocalDateFilter dataValidade() {
        if (dataValidade == null) {
            setDataValidade(new LocalDateFilter());
        }
        return dataValidade;
    }

    public void setDataValidade(LocalDateFilter dataValidade) {
        this.dataValidade = dataValidade;
    }

    public TipoLeiteFilter getTipoLeite() {
        return tipoLeite;
    }

    public Optional<TipoLeiteFilter> optionalTipoLeite() {
        return Optional.ofNullable(tipoLeite);
    }

    public TipoLeiteFilter tipoLeite() {
        if (tipoLeite == null) {
            setTipoLeite(new TipoLeiteFilter());
        }
        return tipoLeite;
    }

    public void setTipoLeite(TipoLeiteFilter tipoLeite) {
        this.tipoLeite = tipoLeite;
    }

    public ClassificacaoLeiteFilter getClassificacao() {
        return classificacao;
    }

    public Optional<ClassificacaoLeiteFilter> optionalClassificacao() {
        return Optional.ofNullable(classificacao);
    }

    public ClassificacaoLeiteFilter classificacao() {
        if (classificacao == null) {
            setClassificacao(new ClassificacaoLeiteFilter());
        }
        return classificacao;
    }

    public void setClassificacao(ClassificacaoLeiteFilter classificacao) {
        this.classificacao = classificacao;
    }

    public DoubleFilter getVolumeTotalMl() {
        return volumeTotalMl;
    }

    public Optional<DoubleFilter> optionalVolumeTotalMl() {
        return Optional.ofNullable(volumeTotalMl);
    }

    public DoubleFilter volumeTotalMl() {
        if (volumeTotalMl == null) {
            setVolumeTotalMl(new DoubleFilter());
        }
        return volumeTotalMl;
    }

    public void setVolumeTotalMl(DoubleFilter volumeTotalMl) {
        this.volumeTotalMl = volumeTotalMl;
    }

    public DoubleFilter getVolumeDisponivelMl() {
        return volumeDisponivelMl;
    }

    public Optional<DoubleFilter> optionalVolumeDisponivelMl() {
        return Optional.ofNullable(volumeDisponivelMl);
    }

    public DoubleFilter volumeDisponivelMl() {
        if (volumeDisponivelMl == null) {
            setVolumeDisponivelMl(new DoubleFilter());
        }
        return volumeDisponivelMl;
    }

    public void setVolumeDisponivelMl(DoubleFilter volumeDisponivelMl) {
        this.volumeDisponivelMl = volumeDisponivelMl;
    }

    public StringFilter getLocalArmazenamento() {
        return localArmazenamento;
    }

    public Optional<StringFilter> optionalLocalArmazenamento() {
        return Optional.ofNullable(localArmazenamento);
    }

    public StringFilter localArmazenamento() {
        if (localArmazenamento == null) {
            setLocalArmazenamento(new StringFilter());
        }
        return localArmazenamento;
    }

    public void setLocalArmazenamento(StringFilter localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }

    public DoubleFilter getTemperaturaArmazenamento() {
        return temperaturaArmazenamento;
    }

    public Optional<DoubleFilter> optionalTemperaturaArmazenamento() {
        return Optional.ofNullable(temperaturaArmazenamento);
    }

    public DoubleFilter temperaturaArmazenamento() {
        if (temperaturaArmazenamento == null) {
            setTemperaturaArmazenamento(new DoubleFilter());
        }
        return temperaturaArmazenamento;
    }

    public void setTemperaturaArmazenamento(DoubleFilter temperaturaArmazenamento) {
        this.temperaturaArmazenamento = temperaturaArmazenamento;
    }

    public StatusLoteFilter getStatusLote() {
        return statusLote;
    }

    public Optional<StatusLoteFilter> optionalStatusLote() {
        return Optional.ofNullable(statusLote);
    }

    public StatusLoteFilter statusLote() {
        if (statusLote == null) {
            setStatusLote(new StatusLoteFilter());
        }
        return statusLote;
    }

    public void setStatusLote(StatusLoteFilter statusLote) {
        this.statusLote = statusLote;
    }

    public LongFilter getDistribuicoesId() {
        return distribuicoesId;
    }

    public Optional<LongFilter> optionalDistribuicoesId() {
        return Optional.ofNullable(distribuicoesId);
    }

    public LongFilter distribuicoesId() {
        if (distribuicoesId == null) {
            setDistribuicoesId(new LongFilter());
        }
        return distribuicoesId;
    }

    public void setDistribuicoesId(LongFilter distribuicoesId) {
        this.distribuicoesId = distribuicoesId;
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
        final EstoqueCriteria that = (EstoqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataProducao, that.dataProducao) &&
            Objects.equals(dataValidade, that.dataValidade) &&
            Objects.equals(tipoLeite, that.tipoLeite) &&
            Objects.equals(classificacao, that.classificacao) &&
            Objects.equals(volumeTotalMl, that.volumeTotalMl) &&
            Objects.equals(volumeDisponivelMl, that.volumeDisponivelMl) &&
            Objects.equals(localArmazenamento, that.localArmazenamento) &&
            Objects.equals(temperaturaArmazenamento, that.temperaturaArmazenamento) &&
            Objects.equals(statusLote, that.statusLote) &&
            Objects.equals(distribuicoesId, that.distribuicoesId) &&
            Objects.equals(processamentoId, that.processamentoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataProducao,
            dataValidade,
            tipoLeite,
            classificacao,
            volumeTotalMl,
            volumeDisponivelMl,
            localArmazenamento,
            temperaturaArmazenamento,
            statusLote,
            distribuicoesId,
            processamentoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstoqueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataProducao().map(f -> "dataProducao=" + f + ", ").orElse("") +
            optionalDataValidade().map(f -> "dataValidade=" + f + ", ").orElse("") +
            optionalTipoLeite().map(f -> "tipoLeite=" + f + ", ").orElse("") +
            optionalClassificacao().map(f -> "classificacao=" + f + ", ").orElse("") +
            optionalVolumeTotalMl().map(f -> "volumeTotalMl=" + f + ", ").orElse("") +
            optionalVolumeDisponivelMl().map(f -> "volumeDisponivelMl=" + f + ", ").orElse("") +
            optionalLocalArmazenamento().map(f -> "localArmazenamento=" + f + ", ").orElse("") +
            optionalTemperaturaArmazenamento().map(f -> "temperaturaArmazenamento=" + f + ", ").orElse("") +
            optionalStatusLote().map(f -> "statusLote=" + f + ", ").orElse("") +
            optionalDistribuicoesId().map(f -> "distribuicoesId=" + f + ", ").orElse("") +
            optionalProcessamentoId().map(f -> "processamentoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
