package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.LocalPreNatal;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.TipoDoadora;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Doadora} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoadoraDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String cartaoSUS;

    @NotNull
    private String cpf;

    @NotNull
    private LocalDate dataNascimento;

    @NotNull
    private String cep;

    @NotNull
    private String estado;

    @NotNull
    private String cidade;

    @NotNull
    private String endereco;

    @NotNull
    private String telefone;

    private String profissao;

    @NotNull
    private TipoDoadora tipoDoadora;

    private LocalPreNatal localPreNatal;

    private ResultadoExame resultadoVDRL;

    private ResultadoExame resultadoHBsAg;

    private ResultadoExame resultadoFTAabs;

    private ResultadoExame resultadoHIV;

    private Boolean transfusaoUltimos5Anos;

    @NotNull
    private LocalDate dataRegistro;

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

    public String getCartaoSUS() {
        return cartaoSUS;
    }

    public void setCartaoSUS(String cartaoSUS) {
        this.cartaoSUS = cartaoSUS;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public TipoDoadora getTipoDoadora() {
        return tipoDoadora;
    }

    public void setTipoDoadora(TipoDoadora tipoDoadora) {
        this.tipoDoadora = tipoDoadora;
    }

    public LocalPreNatal getLocalPreNatal() {
        return localPreNatal;
    }

    public void setLocalPreNatal(LocalPreNatal localPreNatal) {
        this.localPreNatal = localPreNatal;
    }

    public ResultadoExame getResultadoVDRL() {
        return resultadoVDRL;
    }

    public void setResultadoVDRL(ResultadoExame resultadoVDRL) {
        this.resultadoVDRL = resultadoVDRL;
    }

    public ResultadoExame getResultadoHBsAg() {
        return resultadoHBsAg;
    }

    public void setResultadoHBsAg(ResultadoExame resultadoHBsAg) {
        this.resultadoHBsAg = resultadoHBsAg;
    }

    public ResultadoExame getResultadoFTAabs() {
        return resultadoFTAabs;
    }

    public void setResultadoFTAabs(ResultadoExame resultadoFTAabs) {
        this.resultadoFTAabs = resultadoFTAabs;
    }

    public ResultadoExame getResultadoHIV() {
        return resultadoHIV;
    }

    public void setResultadoHIV(ResultadoExame resultadoHIV) {
        this.resultadoHIV = resultadoHIV;
    }

    public Boolean getTransfusaoUltimos5Anos() {
        return transfusaoUltimos5Anos;
    }

    public void setTransfusaoUltimos5Anos(Boolean transfusaoUltimos5Anos) {
        this.transfusaoUltimos5Anos = transfusaoUltimos5Anos;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoadoraDTO)) {
            return false;
        }

        DoadoraDTO doadoraDTO = (DoadoraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, doadoraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoadoraDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cartaoSUS='" + getCartaoSUS() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", cep='" + getCep() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", profissao='" + getProfissao() + "'" +
            ", tipoDoadora='" + getTipoDoadora() + "'" +
            ", localPreNatal='" + getLocalPreNatal() + "'" +
            ", resultadoVDRL='" + getResultadoVDRL() + "'" +
            ", resultadoHBsAg='" + getResultadoHBsAg() + "'" +
            ", resultadoFTAabs='" + getResultadoFTAabs() + "'" +
            ", resultadoHIV='" + getResultadoHIV() + "'" +
            ", transfusaoUltimos5Anos='" + getTransfusaoUltimos5Anos() + "'" +
            ", dataRegistro='" + getDataRegistro() + "'" +
            "}";
    }
}
