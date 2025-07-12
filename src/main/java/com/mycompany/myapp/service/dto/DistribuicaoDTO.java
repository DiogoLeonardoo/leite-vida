package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Distribuicao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DistribuicaoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataDistribuicao;

    @NotNull
    private Double volumeDistribuidoMl;

    @NotNull
    private String responsavelEntrega;

    @NotNull
    private String responsavelRecebimento;

    private String observacoes;

    @NotNull
    private EstoqueDTO estoque;

    @NotNull
    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDistribuicao() {
        return dataDistribuicao;
    }

    public void setDataDistribuicao(LocalDate dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }

    public Double getVolumeDistribuidoMl() {
        return volumeDistribuidoMl;
    }

    public void setVolumeDistribuidoMl(Double volumeDistribuidoMl) {
        this.volumeDistribuidoMl = volumeDistribuidoMl;
    }

    public String getResponsavelEntrega() {
        return responsavelEntrega;
    }

    public void setResponsavelEntrega(String responsavelEntrega) {
        this.responsavelEntrega = responsavelEntrega;
    }

    public String getResponsavelRecebimento() {
        return responsavelRecebimento;
    }

    public void setResponsavelRecebimento(String responsavelRecebimento) {
        this.responsavelRecebimento = responsavelRecebimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public EstoqueDTO getEstoque() {
        return estoque;
    }

    public void setEstoque(EstoqueDTO estoque) {
        this.estoque = estoque;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DistribuicaoDTO)) {
            return false;
        }

        DistribuicaoDTO distribuicaoDTO = (DistribuicaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, distribuicaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistribuicaoDTO{" +
            "id=" + getId() +
            ", dataDistribuicao='" + getDataDistribuicao() + "'" +
            ", volumeDistribuidoMl=" + getVolumeDistribuidoMl() +
            ", responsavelEntrega='" + getResponsavelEntrega() + "'" +
            ", responsavelRecebimento='" + getResponsavelRecebimento() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            ", estoque=" + getEstoque() +
            ", paciente=" + getPaciente() +
            "}";
    }
}
