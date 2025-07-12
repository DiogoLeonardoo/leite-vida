package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.StatusColeta;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Coleta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ColetaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataColeta;

    @NotNull
    private Double volumeMl;

    @NotNull
    private Double temperatura;

    private String localColeta;

    private String observacoes;

    @NotNull
    private StatusColeta statusColeta;

    private ProcessamentoDTO processamento;

    @NotNull
    private DoadoraDTO doadora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public Double getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(Double volumeMl) {
        this.volumeMl = volumeMl;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public String getLocalColeta() {
        return localColeta;
    }

    public void setLocalColeta(String localColeta) {
        this.localColeta = localColeta;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusColeta getStatusColeta() {
        return statusColeta;
    }

    public void setStatusColeta(StatusColeta statusColeta) {
        this.statusColeta = statusColeta;
    }

    public ProcessamentoDTO getProcessamento() {
        return processamento;
    }

    public void setProcessamento(ProcessamentoDTO processamento) {
        this.processamento = processamento;
    }

    public DoadoraDTO getDoadora() {
        return doadora;
    }

    public void setDoadora(DoadoraDTO doadora) {
        this.doadora = doadora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColetaDTO)) {
            return false;
        }

        ColetaDTO coletaDTO = (ColetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coletaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ColetaDTO{" +
            "id=" + getId() +
            ", dataColeta='" + getDataColeta() + "'" +
            ", volumeMl=" + getVolumeMl() +
            ", temperatura=" + getTemperatura() +
            ", localColeta='" + getLocalColeta() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            ", statusColeta='" + getStatusColeta() + "'" +
            ", processamento=" + getProcessamento() +
            ", doadora=" + getDoadora() +
            "}";
    }
}
