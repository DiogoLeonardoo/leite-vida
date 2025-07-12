package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.StatusLote;
import com.mycompany.myapp.domain.enumeration.TipoLeite;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Estoque.
 */
@Entity
@Table(name = "estoque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Estoque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_producao", nullable = false)
    private LocalDate dataProducao;

    @NotNull
    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_leite", nullable = false)
    private TipoLeite tipoLeite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao", nullable = false)
    private ClassificacaoLeite classificacao;

    @NotNull
    @Column(name = "volume_total_ml", nullable = false)
    private Double volumeTotalMl;

    @NotNull
    @Column(name = "volume_disponivel_ml", nullable = false)
    private Double volumeDisponivelMl;

    @NotNull
    @Column(name = "local_armazenamento", nullable = false)
    private String localArmazenamento;

    @NotNull
    @Column(name = "temperatura_armazenamento", nullable = false)
    private Double temperaturaArmazenamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_lote", nullable = false)
    private StatusLote statusLote;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estoque")
    @JsonIgnoreProperties(value = { "estoque", "paciente" }, allowSetters = true)
    private Set<Distribuicao> distribuicoes = new HashSet<>();

    @JsonIgnoreProperties(value = { "estoque", "coleta" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "estoque")
    private Processamento processamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estoque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataProducao() {
        return this.dataProducao;
    }

    public Estoque dataProducao(LocalDate dataProducao) {
        this.setDataProducao(dataProducao);
        return this;
    }

    public void setDataProducao(LocalDate dataProducao) {
        this.dataProducao = dataProducao;
    }

    public LocalDate getDataValidade() {
        return this.dataValidade;
    }

    public Estoque dataValidade(LocalDate dataValidade) {
        this.setDataValidade(dataValidade);
        return this;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public TipoLeite getTipoLeite() {
        return this.tipoLeite;
    }

    public Estoque tipoLeite(TipoLeite tipoLeite) {
        this.setTipoLeite(tipoLeite);
        return this;
    }

    public void setTipoLeite(TipoLeite tipoLeite) {
        this.tipoLeite = tipoLeite;
    }

    public ClassificacaoLeite getClassificacao() {
        return this.classificacao;
    }

    public Estoque classificacao(ClassificacaoLeite classificacao) {
        this.setClassificacao(classificacao);
        return this;
    }

    public void setClassificacao(ClassificacaoLeite classificacao) {
        this.classificacao = classificacao;
    }

    public Double getVolumeTotalMl() {
        return this.volumeTotalMl;
    }

    public Estoque volumeTotalMl(Double volumeTotalMl) {
        this.setVolumeTotalMl(volumeTotalMl);
        return this;
    }

    public void setVolumeTotalMl(Double volumeTotalMl) {
        this.volumeTotalMl = volumeTotalMl;
    }

    public Double getVolumeDisponivelMl() {
        return this.volumeDisponivelMl;
    }

    public Estoque volumeDisponivelMl(Double volumeDisponivelMl) {
        this.setVolumeDisponivelMl(volumeDisponivelMl);
        return this;
    }

    public void setVolumeDisponivelMl(Double volumeDisponivelMl) {
        this.volumeDisponivelMl = volumeDisponivelMl;
    }

    public String getLocalArmazenamento() {
        return this.localArmazenamento;
    }

    public Estoque localArmazenamento(String localArmazenamento) {
        this.setLocalArmazenamento(localArmazenamento);
        return this;
    }

    public void setLocalArmazenamento(String localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }

    public Double getTemperaturaArmazenamento() {
        return this.temperaturaArmazenamento;
    }

    public Estoque temperaturaArmazenamento(Double temperaturaArmazenamento) {
        this.setTemperaturaArmazenamento(temperaturaArmazenamento);
        return this;
    }

    public void setTemperaturaArmazenamento(Double temperaturaArmazenamento) {
        this.temperaturaArmazenamento = temperaturaArmazenamento;
    }

    public StatusLote getStatusLote() {
        return this.statusLote;
    }

    public Estoque statusLote(StatusLote statusLote) {
        this.setStatusLote(statusLote);
        return this;
    }

    public void setStatusLote(StatusLote statusLote) {
        this.statusLote = statusLote;
    }

    public Set<Distribuicao> getDistribuicoes() {
        return this.distribuicoes;
    }

    public void setDistribuicoes(Set<Distribuicao> distribuicaos) {
        if (this.distribuicoes != null) {
            this.distribuicoes.forEach(i -> i.setEstoque(null));
        }
        if (distribuicaos != null) {
            distribuicaos.forEach(i -> i.setEstoque(this));
        }
        this.distribuicoes = distribuicaos;
    }

    public Estoque distribuicoes(Set<Distribuicao> distribuicaos) {
        this.setDistribuicoes(distribuicaos);
        return this;
    }

    public Estoque addDistribuicoes(Distribuicao distribuicao) {
        this.distribuicoes.add(distribuicao);
        distribuicao.setEstoque(this);
        return this;
    }

    public Estoque removeDistribuicoes(Distribuicao distribuicao) {
        this.distribuicoes.remove(distribuicao);
        distribuicao.setEstoque(null);
        return this;
    }

    public Processamento getProcessamento() {
        return this.processamento;
    }

    public void setProcessamento(Processamento processamento) {
        if (this.processamento != null) {
            this.processamento.setEstoque(null);
        }
        if (processamento != null) {
            processamento.setEstoque(this);
        }
        this.processamento = processamento;
    }

    public Estoque processamento(Processamento processamento) {
        this.setProcessamento(processamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estoque)) {
            return false;
        }
        return getId() != null && getId().equals(((Estoque) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estoque{" +
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
