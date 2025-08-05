package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public class RelatorioColetaDTO {

    private Long coletaId;
    private LocalDate dataColeta;
    private Integer volumeMl;
    private String localColeta;
    private String statusColeta;
    private String nomeDoadora;
    private String cpfDoadora;
    private String telefoneDoadora;

    public RelatorioColetaDTO() {}

    public RelatorioColetaDTO(
        Long coletaId,
        LocalDate dataColeta,
        Integer volumeMl,
        String localColeta,
        String statusColeta,
        String nomeDoadora,
        String cpfDoadora,
        String telefoneDoadora
    ) {
        this.coletaId = coletaId;
        this.dataColeta = dataColeta;
        this.volumeMl = volumeMl;
        this.localColeta = localColeta;
        this.statusColeta = statusColeta;
        this.nomeDoadora = nomeDoadora;
        this.cpfDoadora = cpfDoadora;
        this.telefoneDoadora = telefoneDoadora;
    }

    public Long getColetaId() {
        return coletaId;
    }

    public void setColetaId(Long coletaId) {
        this.coletaId = coletaId;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public Integer getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(Integer volumeMl) {
        this.volumeMl = volumeMl;
    }

    public String getLocalColeta() {
        return localColeta;
    }

    public void setLocalColeta(String localColeta) {
        this.localColeta = localColeta;
    }

    public String getStatusColeta() {
        return statusColeta;
    }

    public void setStatusColeta(String statusColeta) {
        this.statusColeta = statusColeta;
    }

    public String getNomeDoadora() {
        return nomeDoadora;
    }

    public void setNomeDoadora(String nomeDoadora) {
        this.nomeDoadora = nomeDoadora;
    }

    public String getCpfDoadora() {
        return cpfDoadora;
    }

    public void setCpfDoadora(String cpfDoadora) {
        this.cpfDoadora = cpfDoadora;
    }

    public String getTelefoneDoadora() {
        return telefoneDoadora;
    }

    public void setTelefoneDoadora(String telefoneDoadora) {
        this.telefoneDoadora = telefoneDoadora;
    }
}
