package com.mycompany.myapp.service.dto;

public class DistribuicaoRequestDTO {

    private Long estoqueId;

    private Long pacienteId;

    private Double volumeDistribuidoMl;

    private String responsavelEntrega;

    private String responsavelRecebimento;

    private String observacoes;

    public Long getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(Long estoqueId) {
        this.estoqueId = estoqueId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
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
}
