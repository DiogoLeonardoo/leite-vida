package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ResultadoAnalise;
import com.mycompany.myapp.domain.enumeration.StatusProcessamento;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Processamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessamentoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataProcessamento;

    @NotNull
    private String tecnicoResponsavel;

    private Double valorAcidezDornic;

    private Double valorCaloricoKcal;

    @NotNull
    private ResultadoAnalise resultadoAnalise;

    @NotNull
    private StatusProcessamento statusProcessamento;

    private EstoqueDTO estoque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(LocalDate dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public String getTecnicoResponsavel() {
        return tecnicoResponsavel;
    }

    public void setTecnicoResponsavel(String tecnicoResponsavel) {
        this.tecnicoResponsavel = tecnicoResponsavel;
    }

    public Double getValorAcidezDornic() {
        return valorAcidezDornic;
    }

    public void setValorAcidezDornic(Double valorAcidezDornic) {
        this.valorAcidezDornic = valorAcidezDornic;
    }

    public Double getValorCaloricoKcal() {
        return valorCaloricoKcal;
    }

    public void setValorCaloricoKcal(Double valorCaloricoKcal) {
        this.valorCaloricoKcal = valorCaloricoKcal;
    }

    public ResultadoAnalise getResultadoAnalise() {
        return resultadoAnalise;
    }

    public void setResultadoAnalise(ResultadoAnalise resultadoAnalise) {
        this.resultadoAnalise = resultadoAnalise;
    }

    public StatusProcessamento getStatusProcessamento() {
        return statusProcessamento;
    }

    public void setStatusProcessamento(StatusProcessamento statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public EstoqueDTO getEstoque() {
        return estoque;
    }

    public void setEstoque(EstoqueDTO estoque) {
        this.estoque = estoque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessamentoDTO)) {
            return false;
        }

        ProcessamentoDTO processamentoDTO = (ProcessamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessamentoDTO{" +
            "id=" + getId() +
            ", dataProcessamento='" + getDataProcessamento() + "'" +
            ", tecnicoResponsavel='" + getTecnicoResponsavel() + "'" +
            ", valorAcidezDornic=" + getValorAcidezDornic() +
            ", valorCaloricoKcal=" + getValorCaloricoKcal() +
            ", resultadoAnalise='" + getResultadoAnalise() + "'" +
            ", statusProcessamento='" + getStatusProcessamento() + "'" +
            ", estoque=" + getEstoque() +
            "}";
    }
}
