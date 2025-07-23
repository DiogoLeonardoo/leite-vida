package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.StatusColeta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Coleta.
 */
@Entity
@Table(name = "coleta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coleta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_coleta", nullable = false)
    private LocalDate dataColeta;

    @NotNull
    @Column(name = "volume_ml", nullable = false)
    private Double volumeMl;

    @NotNull
    @Column(name = "temperatura", nullable = false)
    private Double temperatura;

    @Column(name = "local_coleta")
    private String localColeta;

    @Column(name = "observacoes")
    private String observacoes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_coleta", nullable = false)
    private StatusColeta statusColeta;

    // ALTERAÇÃO: Removido @OneToOne e @JoinColumn, mantido apenas o mapeamento bidirecional
    @JsonIgnoreProperties(value = { "estoque", "coleta" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "coleta")
    private Processamento processamento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "coletas" }, allowSetters = true)
    private Doadora doadora;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coleta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataColeta() {
        return this.dataColeta;
    }

    public Coleta dataColeta(LocalDate dataColeta) {
        this.setDataColeta(dataColeta);
        return this;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public Double getVolumeMl() {
        return this.volumeMl;
    }

    public Coleta volumeMl(Double volumeMl) {
        this.setVolumeMl(volumeMl);
        return this;
    }

    public void setVolumeMl(Double volumeMl) {
        this.volumeMl = volumeMl;
    }

    public Double getTemperatura() {
        return this.temperatura;
    }

    public Coleta temperatura(Double temperatura) {
        this.setTemperatura(temperatura);
        return this;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public String getLocalColeta() {
        return this.localColeta;
    }

    public Coleta localColeta(String localColeta) {
        this.setLocalColeta(localColeta);
        return this;
    }

    public void setLocalColeta(String localColeta) {
        this.localColeta = localColeta;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public Coleta observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusColeta getStatusColeta() {
        return this.statusColeta;
    }

    public Coleta statusColeta(StatusColeta statusColeta) {
        this.setStatusColeta(statusColeta);
        return this;
    }

    public void setStatusColeta(StatusColeta statusColeta) {
        this.statusColeta = statusColeta;
    }

    public Processamento getProcessamento() {
        return this.processamento;
    }

    // ALTERAÇÃO: Método setter simplificado (relacionamento gerenciado por Processamento)
    public void setProcessamento(Processamento processamento) {
        this.processamento = processamento;
    }

    public Coleta processamento(Processamento processamento) {
        this.setProcessamento(processamento);
        return this;
    }

    public Doadora getDoadora() {
        return this.doadora;
    }

    public void setDoadora(Doadora doadora) {
        this.doadora = doadora;
    }

    public Coleta doadora(Doadora doadora) {
        this.setDoadora(doadora);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coleta)) {
            return false;
        }
        return getId() != null && getId().equals(((Coleta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coleta{" +
            "id=" + getId() +
            ", dataColeta='" + getDataColeta() + "'" +
            ", volumeMl=" + getVolumeMl() +
            ", temperatura=" + getTemperatura() +
            ", localColeta='" + getLocalColeta() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            ", statusColeta='" + getStatusColeta() + "'" +
            "}";
    }
}
