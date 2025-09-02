package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ResultadoAnalise;
import com.mycompany.myapp.domain.enumeration.StatusProcessamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Processamento.
 */
@Entity
@Table(name = "processamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Processamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_processamento", nullable = false)
    private LocalDate dataProcessamento;

    @NotNull
    @Column(name = "tecnico_responsavel", nullable = false)
    private String tecnicoResponsavel;

    @Column(name = "valor_acidez_dornic")
    private Double valorAcidezDornic;

    @Column(name = "valor_calorico_kcal")
    private Double valorCaloricoKcal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_analise", nullable = false)
    private ResultadoAnalise resultadoAnalise;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_processamento", nullable = false)
    private StatusProcessamento statusProcessamento;

    @JsonIgnoreProperties(value = { "distribuicoes", "processamento" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Estoque estoque;

    // ALTERAÇÃO: Agora o Processamento possui a chave estrangeira para Coleta
    @JsonIgnoreProperties(value = { "processamento", "doadora" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleta_id", unique = true)
    private Coleta coleta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Processamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataProcessamento() {
        return this.dataProcessamento;
    }

    public Processamento dataProcessamento(LocalDate dataProcessamento) {
        this.setDataProcessamento(dataProcessamento);
        return this;
    }

    public void setDataProcessamento(LocalDate dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public String getTecnicoResponsavel() {
        return this.tecnicoResponsavel;
    }

    public Processamento tecnicoResponsavel(String tecnicoResponsavel) {
        this.setTecnicoResponsavel(tecnicoResponsavel);
        return this;
    }

    public void setTecnicoResponsavel(String tecnicoResponsavel) {
        this.tecnicoResponsavel = tecnicoResponsavel;
    }

    public Double getValorAcidezDornic() {
        return this.valorAcidezDornic;
    }

    public Processamento valorAcidezDornic(Double valorAcidezDornic) {
        this.setValorAcidezDornic(valorAcidezDornic);
        return this;
    }

    public void setValorAcidezDornic(Double valorAcidezDornic) {
        this.valorAcidezDornic = valorAcidezDornic;
    }

    public Double getValorCaloricoKcal() {
        return this.valorCaloricoKcal;
    }

    public Processamento valorCaloricoKcal(Double valorCaloricoKcal) {
        this.setValorCaloricoKcal(valorCaloricoKcal);
        return this;
    }

    public void setValorCaloricoKcal(Double valorCaloricoKcal) {
        this.valorCaloricoKcal = valorCaloricoKcal;
    }

    public ResultadoAnalise getResultadoAnalise() {
        return this.resultadoAnalise;
    }

    public Processamento resultadoAnalise(ResultadoAnalise resultadoAnalise) {
        this.setResultadoAnalise(resultadoAnalise);
        return this;
    }

    public void setResultadoAnalise(ResultadoAnalise resultadoAnalise) {
        this.resultadoAnalise = resultadoAnalise;
    }

    public StatusProcessamento getStatusProcessamento() {
        return this.statusProcessamento;
    }

    public Processamento statusProcessamento(StatusProcessamento statusProcessamento) {
        this.setStatusProcessamento(statusProcessamento);
        return this;
    }

    public void setStatusProcessamento(StatusProcessamento statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public Estoque getEstoque() {
        return this.estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Processamento estoque(Estoque estoque) {
        this.setEstoque(estoque);
        return this;
    }

    public Coleta getColeta() {
        return this.coleta;
    }

    // ALTERAÇÃO: Método setter atualizado para gerenciar o relacionamento bidirecional
    public void setColeta(Coleta coleta) {
        if (this.coleta != null) {
            this.coleta.setProcessamento(null);
        }
        if (coleta != null) {
            coleta.setProcessamento(this);
        }
        this.coleta = coleta;
    }

    public Processamento coleta(Coleta coleta) {
        this.setColeta(coleta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Processamento)) {
            return false;
        }
        return getId() != null && getId().equals(((Processamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Processamento{" +
            "id=" + getId() +
            ", dataProcessamento='" + getDataProcessamento() + "'" +
            ", tecnicoResponsavel='" + getTecnicoResponsavel() + "'" +
            ", valorAcidezDornic=" + getValorAcidezDornic() +
            ", valorCaloricoKcal=" + getValorCaloricoKcal() +
            ", resultadoAnalise='" + getResultadoAnalise() + "'" +
            ", statusProcessamento='" + getStatusProcessamento() + "'" +
            "}";
    }
}
