package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "registro_hospitalar", nullable = false, unique = true)
    private String registroHospitalar;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "peso_nascimento")
    private Double pesoNascimento;

    @Column(name = "idade_gestacional")
    private Integer idadeGestacional;

    @NotNull
    @Column(name = "condicao_clinica", nullable = false)
    private String condicaoClinica;

    @NotNull
    @Column(name = "nome_responsavel", nullable = false)
    private String nomeResponsavel;

    @NotNull
    @Column(name = "cpf_responsavel", nullable = false)
    private String cpfResponsavel;

    @NotNull
    @Column(name = "telefone_responsavel", nullable = false)
    private String telefoneResponsavel;

    @NotNull
    @Column(name = "parentesco_responsavel", nullable = false)
    private String parentescoResponsavel;

    @NotNull
    @Column(name = "status_ativo", nullable = false)
    private Boolean statusAtivo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paciente")
    @JsonIgnoreProperties(value = { "estoque", "paciente" }, allowSetters = true)
    private Set<Distribuicao> distribuicoes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Paciente nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistroHospitalar() {
        return this.registroHospitalar;
    }

    public Paciente registroHospitalar(String registroHospitalar) {
        this.setRegistroHospitalar(registroHospitalar);
        return this;
    }

    public void setRegistroHospitalar(String registroHospitalar) {
        this.registroHospitalar = registroHospitalar;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Paciente dataNascimento(LocalDate dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Double getPesoNascimento() {
        return this.pesoNascimento;
    }

    public Paciente pesoNascimento(Double pesoNascimento) {
        this.setPesoNascimento(pesoNascimento);
        return this;
    }

    public void setPesoNascimento(Double pesoNascimento) {
        this.pesoNascimento = pesoNascimento;
    }

    public Integer getIdadeGestacional() {
        return this.idadeGestacional;
    }

    public Paciente idadeGestacional(Integer idadeGestacional) {
        this.setIdadeGestacional(idadeGestacional);
        return this;
    }

    public void setIdadeGestacional(Integer idadeGestacional) {
        this.idadeGestacional = idadeGestacional;
    }

    public String getCondicaoClinica() {
        return this.condicaoClinica;
    }

    public Paciente condicaoClinica(String condicaoClinica) {
        this.setCondicaoClinica(condicaoClinica);
        return this;
    }

    public void setCondicaoClinica(String condicaoClinica) {
        this.condicaoClinica = condicaoClinica;
    }

    public String getNomeResponsavel() {
        return this.nomeResponsavel;
    }

    public Paciente nomeResponsavel(String nomeResponsavel) {
        this.setNomeResponsavel(nomeResponsavel);
        return this;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getCpfResponsavel() {
        return this.cpfResponsavel;
    }

    public Paciente cpfResponsavel(String cpfResponsavel) {
        this.setCpfResponsavel(cpfResponsavel);
        return this;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getTelefoneResponsavel() {
        return this.telefoneResponsavel;
    }

    public Paciente telefoneResponsavel(String telefoneResponsavel) {
        this.setTelefoneResponsavel(telefoneResponsavel);
        return this;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public String getParentescoResponsavel() {
        return this.parentescoResponsavel;
    }

    public Paciente parentescoResponsavel(String parentescoResponsavel) {
        this.setParentescoResponsavel(parentescoResponsavel);
        return this;
    }

    public void setParentescoResponsavel(String parentescoResponsavel) {
        this.parentescoResponsavel = parentescoResponsavel;
    }

    public Boolean getStatusAtivo() {
        return this.statusAtivo;
    }

    public Paciente statusAtivo(Boolean statusAtivo) {
        this.setStatusAtivo(statusAtivo);
        return this;
    }

    public void setStatusAtivo(Boolean statusAtivo) {
        this.statusAtivo = statusAtivo;
    }

    public Set<Distribuicao> getDistribuicoes() {
        return this.distribuicoes;
    }

    public void setDistribuicoes(Set<Distribuicao> distribuicaos) {
        if (this.distribuicoes != null) {
            this.distribuicoes.forEach(i -> i.setPaciente(null));
        }
        if (distribuicaos != null) {
            distribuicaos.forEach(i -> i.setPaciente(this));
        }
        this.distribuicoes = distribuicaos;
    }

    public Paciente distribuicoes(Set<Distribuicao> distribuicaos) {
        this.setDistribuicoes(distribuicaos);
        return this;
    }

    public Paciente addDistribuicoes(Distribuicao distribuicao) {
        this.distribuicoes.add(distribuicao);
        distribuicao.setPaciente(this);
        return this;
    }

    public Paciente removeDistribuicoes(Distribuicao distribuicao) {
        this.distribuicoes.remove(distribuicao);
        distribuicao.setPaciente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
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
