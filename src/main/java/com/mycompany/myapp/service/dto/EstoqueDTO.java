package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Estoque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstoqueDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataProducao;

    @NotNull
    private LocalDate dataValidade;

    @NotNull
    private TipoLeite tipoLeite;

    @NotNull
    private ClassificacaoLeite classificacao;

    @NotNull
    private Double volumeTotalMl;

    @NotNull
    private Double volumeDisponivelMl;

    @NotNull
    private String localArmazenamento;

    @NotNull
    private Double temperaturaArmazenamento;

    @NotNull
    private StatusLote statusLote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataProducao() {
        return dataProducao;
    }

    public void setDataProducao(LocalDate dataProducao) {
        this.dataProducao = dataProducao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public TipoLeite getTipoLeite() {
        return tipoLeite;
    }

    public void setTipoLeite(TipoLeite tipoLeite) {
        this.tipoLeite = tipoLeite;
    }

    public ClassificacaoLeite getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(ClassificacaoLeite classificacao) {
        this.classificacao = classificacao;
    }

    public Double getVolumeTotalMl() {
        return volumeTotalMl;
    }

    public void setVolumeTotalMl(Double volumeTotalMl) {
        this.volumeTotalMl = volumeTotalMl;
    }

    public Double getVolumeDisponivelMl() {
        return volumeDisponivelMl;
    }

    public void setVolumeDisponivelMl(Double volumeDisponivelMl) {
        this.volumeDisponivelMl = volumeDisponivelMl;
    }

    public String getLocalArmazenamento() {
        return localArmazenamento;
    }

    public void setLocalArmazenamento(String localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }

    public Double getTemperaturaArmazenamento() {
        return temperaturaArmazenamento;
    }

    public void setTemperaturaArmazenamento(Double temperaturaArmazenamento) {
        this.temperaturaArmazenamento = temperaturaArmazenamento;
    }

    public StatusLote getStatusLote() {
        return statusLote;
    }

    public void setStatusLote(StatusLote statusLote) {
        this.statusLote = statusLote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstoqueDTO)) {
            return false;
        }

        EstoqueDTO estoqueDTO = (EstoqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, estoqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstoqueDTO{" +
            "id=" + getId() +
            ", dataProducao='" + getDataProducao() + "'" +
            ", dataValidade='" + getDataValidade() + "'" +
            ", tipoLeite='" + getTipoLeite() + "'" +
            ", classificacao='" + getClassificacao() + "'" +
            ", volumeTotalMl=" + getVolumeTotalMl() +
            ", volumeDisponivelMl=" + getVolumeDisponivelMl() +
            ", localArmazenamento='" + getLocalArmazenamento() + "'" +
            ", temperaturaArmazenamento=" + getTemperaturaArmazenamento() +
            ", statusLote='" + getStatusLote() + "'" +
            "}";
    }
}
