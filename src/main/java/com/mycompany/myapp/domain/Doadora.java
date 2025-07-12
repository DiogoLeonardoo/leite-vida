package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.LocalPreNatal;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.TipoDoadora;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Doadora.
 */
@Entity
@Table(name = "doadora")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Doadora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cartao_sus", unique = true)
    private String cartaoSUS;

    @NotNull
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotNull
    @Column(name = "cep", nullable = false)
    private String cep;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull
    @Column(name = "cidade", nullable = false)
    private String cidade;

    @NotNull
    @Column(name = "endereco", nullable = false)
    private String endereco;

    @NotNull
    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "profissao")
    private String profissao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doadora", nullable = false)
    private TipoDoadora tipoDoadora;

    @Enumerated(EnumType.STRING)
    @Column(name = "local_pre_natal")
    private LocalPreNatal localPreNatal;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_vdrl")
    private ResultadoExame resultadoVDRL;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_h_bs_ag")
    private ResultadoExame resultadoHBsAg;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_ft_aabs")
    private ResultadoExame resultadoFTAabs;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_hiv")
    private ResultadoExame resultadoHIV;

    @Column(name = "transfusao_ultimos_5_anos")
    private Boolean transfusaoUltimos5Anos;

    @NotNull
    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doadora")
    @JsonIgnoreProperties(value = { "processamento", "doadora" }, allowSetters = true)
    private Set<Coleta> coletas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Doadora id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Doadora nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCartaoSUS() {
        return this.cartaoSUS;
    }

    public Doadora cartaoSUS(String cartaoSUS) {
        this.setCartaoSUS(cartaoSUS);
        return this;
    }

    public void setCartaoSUS(String cartaoSUS) {
        this.cartaoSUS = cartaoSUS;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Doadora cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Doadora dataNascimento(LocalDate dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCep() {
        return this.cep;
    }

    public Doadora cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return this.estado;
    }

    public Doadora estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return this.cidade;
    }

    public Doadora cidade(String cidade) {
        this.setCidade(cidade);
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Doadora endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Doadora telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getProfissao() {
        return this.profissao;
    }

    public Doadora profissao(String profissao) {
        this.setProfissao(profissao);
        return this;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public TipoDoadora getTipoDoadora() {
        return this.tipoDoadora;
    }

    public Doadora tipoDoadora(TipoDoadora tipoDoadora) {
        this.setTipoDoadora(tipoDoadora);
        return this;
    }

    public void setTipoDoadora(TipoDoadora tipoDoadora) {
        this.tipoDoadora = tipoDoadora;
    }

    public LocalPreNatal getLocalPreNatal() {
        return this.localPreNatal;
    }

    public Doadora localPreNatal(LocalPreNatal localPreNatal) {
        this.setLocalPreNatal(localPreNatal);
        return this;
    }

    public void setLocalPreNatal(LocalPreNatal localPreNatal) {
        this.localPreNatal = localPreNatal;
    }

    public ResultadoExame getResultadoVDRL() {
        return this.resultadoVDRL;
    }

    public Doadora resultadoVDRL(ResultadoExame resultadoVDRL) {
        this.setResultadoVDRL(resultadoVDRL);
        return this;
    }

    public void setResultadoVDRL(ResultadoExame resultadoVDRL) {
        this.resultadoVDRL = resultadoVDRL;
    }

    public ResultadoExame getResultadoHBsAg() {
        return this.resultadoHBsAg;
    }

    public Doadora resultadoHBsAg(ResultadoExame resultadoHBsAg) {
        this.setResultadoHBsAg(resultadoHBsAg);
        return this;
    }

    public void setResultadoHBsAg(ResultadoExame resultadoHBsAg) {
        this.resultadoHBsAg = resultadoHBsAg;
    }

    public ResultadoExame getResultadoFTAabs() {
        return this.resultadoFTAabs;
    }

    public Doadora resultadoFTAabs(ResultadoExame resultadoFTAabs) {
        this.setResultadoFTAabs(resultadoFTAabs);
        return this;
    }

    public void setResultadoFTAabs(ResultadoExame resultadoFTAabs) {
        this.resultadoFTAabs = resultadoFTAabs;
    }

    public ResultadoExame getResultadoHIV() {
        return this.resultadoHIV;
    }

    public Doadora resultadoHIV(ResultadoExame resultadoHIV) {
        this.setResultadoHIV(resultadoHIV);
        return this;
    }

    public void setResultadoHIV(ResultadoExame resultadoHIV) {
        this.resultadoHIV = resultadoHIV;
    }

    public Boolean getTransfusaoUltimos5Anos() {
        return this.transfusaoUltimos5Anos;
    }

    public Doadora transfusaoUltimos5Anos(Boolean transfusaoUltimos5Anos) {
        this.setTransfusaoUltimos5Anos(transfusaoUltimos5Anos);
        return this;
    }

    public void setTransfusaoUltimos5Anos(Boolean transfusaoUltimos5Anos) {
        this.transfusaoUltimos5Anos = transfusaoUltimos5Anos;
    }

    public LocalDate getDataRegistro() {
        return this.dataRegistro;
    }

    public Doadora dataRegistro(LocalDate dataRegistro) {
        this.setDataRegistro(dataRegistro);
        return this;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Set<Coleta> getColetas() {
        return this.coletas;
    }

    public void setColetas(Set<Coleta> coletas) {
        if (this.coletas != null) {
            this.coletas.forEach(i -> i.setDoadora(null));
        }
        if (coletas != null) {
            coletas.forEach(i -> i.setDoadora(this));
        }
        this.coletas = coletas;
    }

    public Doadora coletas(Set<Coleta> coletas) {
        this.setColetas(coletas);
        return this;
    }

    public Doadora addColetas(Coleta coleta) {
        this.coletas.add(coleta);
        coleta.setDoadora(this);
        return this;
    }

    public Doadora removeColetas(Coleta coleta) {
        this.coletas.remove(coleta);
        coleta.setDoadora(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doadora)) {
            return false;
        }
        return getId() != null && getId().equals(((Doadora) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doadora{" +
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
