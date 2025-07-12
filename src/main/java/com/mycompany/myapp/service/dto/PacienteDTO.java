package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Paciente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String registroHospitalar;

    @NotNull
    private LocalDate dataNascimento;

    private Double pesoNascimento;

    private Integer idadeGestacional;

    @NotNull
    private String condicaoClinica;

    @NotNull
    private String nomeResponsavel;

    @NotNull
    private String cpfResponsavel;

    @NotNull
    private String telefoneResponsavel;

    @NotNull
    private String parentescoResponsavel;

    @NotNull
    private Boolean statusAtivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistroHospitalar() {
        return registroHospitalar;
    }

    public void setRegistroHospitalar(String registroHospitalar) {
        this.registroHospitalar = registroHospitalar;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Double getPesoNascimento() {
        return pesoNascimento;
    }

    public void setPesoNascimento(Double pesoNascimento) {
        this.pesoNascimento = pesoNascimento;
    }

    public Integer getIdadeGestacional() {
        return idadeGestacional;
    }

    public void setIdadeGestacional(Integer idadeGestacional) {
        this.idadeGestacional = idadeGestacional;
    }

    public String getCondicaoClinica() {
        return condicaoClinica;
    }

    public void setCondicaoClinica(String condicaoClinica) {
        this.condicaoClinica = condicaoClinica;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public String getParentescoResponsavel() {
        return parentescoResponsavel;
    }

    public void setParentescoResponsavel(String parentescoResponsavel) {
        this.parentescoResponsavel = parentescoResponsavel;
    }

    public Boolean getStatusAtivo() {
        return statusAtivo;
    }

    public void setStatusAtivo(Boolean statusAtivo) {
        this.statusAtivo = statusAtivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteDTO)) {
            return false;
        }

        PacienteDTO pacienteDTO = (PacienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", registroHospitalar='" + getRegistroHospitalar() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", pesoNascimento=" + getPesoNascimento() +
            ", idadeGestacional=" + getIdadeGestacional() +
            ", condicaoClinica='" + getCondicaoClinica() + "'" +
            ", nomeResponsavel='" + getNomeResponsavel() + "'" +
            ", cpfResponsavel='" + getCpfResponsavel() + "'" +
            ", telefoneResponsavel='" + getTelefoneResponsavel() + "'" +
            ", parentescoResponsavel='" + getParentescoResponsavel() + "'" +
            ", statusAtivo='" + getStatusAtivo() + "'" +
            "}";
    }
}
