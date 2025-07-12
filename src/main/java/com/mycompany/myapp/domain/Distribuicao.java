package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Distribuicao.
 */
@Entity
@Table(name = "distribuicao")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Distribuicao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_distribuicao", nullable = false)
    private LocalDate dataDistribuicao;

    @NotNull
    @Column(name = "volume_distribuido_ml", nullable = false)
    private Double volumeDistribuidoMl;

    @NotNull
    @Column(name = "responsavel_entrega", nullable = false)
    private String responsavelEntrega;

    @NotNull
    @Column(name = "responsavel_recebimento", nullable = false)
    private String responsavelRecebimento;

    @Column(name = "observacoes")
    private String observacoes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "distribuicoes", "processamento" }, allowSetters = true)
    private Estoque estoque;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "distribuicoes" }, allowSetters = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Distribuicao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDistribuicao() {
        return this.dataDistribuicao;
    }

    public Distribuicao dataDistribuicao(LocalDate dataDistribuicao) {
        this.setDataDistribuicao(dataDistribuicao);
        return this;
    }

    public void setDataDistribuicao(LocalDate dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }

    public Double getVolumeDistribuidoMl() {
        return this.volumeDistribuidoMl;
    }

    public Distribuicao volumeDistribuidoMl(Double volumeDistribuidoMl) {
        this.setVolumeDistribuidoMl(volumeDistribuidoMl);
        return this;
    }

    public void setVolumeDistribuidoMl(Double volumeDistribuidoMl) {
        this.volumeDistribuidoMl = volumeDistribuidoMl;
    }

    public String getResponsavelEntrega() {
        return this.responsavelEntrega;
    }

    public Distribuicao responsavelEntrega(String responsavelEntrega) {
        this.setResponsavelEntrega(responsavelEntrega);
        return this;
    }

    public void setResponsavelEntrega(String responsavelEntrega) {
        this.responsavelEntrega = responsavelEntrega;
    }

    public String getResponsavelRecebimento() {
        return this.responsavelRecebimento;
    }

    public Distribuicao responsavelRecebimento(String responsavelRecebimento) {
        this.setResponsavelRecebimento(responsavelRecebimento);
        return this;
    }

    public void setResponsavelRecebimento(String responsavelRecebimento) {
        this.responsavelRecebimento = responsavelRecebimento;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public Distribuicao observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Estoque getEstoque() {
        return this.estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Distribuicao estoque(Estoque estoque) {
        this.setEstoque(estoque);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Distribuicao paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distribuicao)) {
            return false;
        }
        return getId() != null && getId().equals(((Distribuicao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Distribuicao{" +
            "id=" + getId() +
            ", dataDistribuicao='" + getDataDistribuicao() + "'" +
            ", volumeDistribuidoMl=" + getVolumeDistribuidoMl() +
            ", responsavelEntrega='" + getResponsavelEntrega() + "'" +
            ", responsavelRecebimento='" + getResponsavelRecebimento() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            "}";
    }
}
