package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Paciente} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter registroHospitalar;

    private LocalDateFilter dataNascimento;

    private DoubleFilter pesoNascimento;

    private IntegerFilter idadeGestacional;

    private StringFilter condicaoClinica;

    private StringFilter nomeResponsavel;

    private StringFilter cpfResponsavel;

    private StringFilter telefoneResponsavel;

    private StringFilter parentescoResponsavel;

    private BooleanFilter statusAtivo;

    private LongFilter distribuicoesId;

    private Boolean distinct;

    public PacienteCriteria() {}

    public PacienteCriteria(PacienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.registroHospitalar = other.optionalRegistroHospitalar().map(StringFilter::copy).orElse(null);
        this.dataNascimento = other.optionalDataNascimento().map(LocalDateFilter::copy).orElse(null);
        this.pesoNascimento = other.optionalPesoNascimento().map(DoubleFilter::copy).orElse(null);
        this.idadeGestacional = other.optionalIdadeGestacional().map(IntegerFilter::copy).orElse(null);
        this.condicaoClinica = other.optionalCondicaoClinica().map(StringFilter::copy).orElse(null);
        this.nomeResponsavel = other.optionalNomeResponsavel().map(StringFilter::copy).orElse(null);
        this.cpfResponsavel = other.optionalCpfResponsavel().map(StringFilter::copy).orElse(null);
        this.telefoneResponsavel = other.optionalTelefoneResponsavel().map(StringFilter::copy).orElse(null);
        this.parentescoResponsavel = other.optionalParentescoResponsavel().map(StringFilter::copy).orElse(null);
        this.statusAtivo = other.optionalStatusAtivo().map(BooleanFilter::copy).orElse(null);
        this.distribuicoesId = other.optionalDistribuicoesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PacienteCriteria copy() {
        return new PacienteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getRegistroHospitalar() {
        return registroHospitalar;
    }

    public Optional<StringFilter> optionalRegistroHospitalar() {
        return Optional.ofNullable(registroHospitalar);
    }

    public StringFilter registroHospitalar() {
        if (registroHospitalar == null) {
            setRegistroHospitalar(new StringFilter());
        }
        return registroHospitalar;
    }

    public void setRegistroHospitalar(StringFilter registroHospitalar) {
        this.registroHospitalar = registroHospitalar;
    }

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public Optional<LocalDateFilter> optionalDataNascimento() {
        return Optional.ofNullable(dataNascimento);
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            setDataNascimento(new LocalDateFilter());
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public DoubleFilter getPesoNascimento() {
        return pesoNascimento;
    }

    public Optional<DoubleFilter> optionalPesoNascimento() {
        return Optional.ofNullable(pesoNascimento);
    }

    public DoubleFilter pesoNascimento() {
        if (pesoNascimento == null) {
            setPesoNascimento(new DoubleFilter());
        }
        return pesoNascimento;
    }

    public void setPesoNascimento(DoubleFilter pesoNascimento) {
        this.pesoNascimento = pesoNascimento;
    }

    public IntegerFilter getIdadeGestacional() {
        return idadeGestacional;
    }

    public Optional<IntegerFilter> optionalIdadeGestacional() {
        return Optional.ofNullable(idadeGestacional);
    }

    public IntegerFilter idadeGestacional() {
        if (idadeGestacional == null) {
            setIdadeGestacional(new IntegerFilter());
        }
        return idadeGestacional;
    }

    public void setIdadeGestacional(IntegerFilter idadeGestacional) {
        this.idadeGestacional = idadeGestacional;
    }

    public StringFilter getCondicaoClinica() {
        return condicaoClinica;
    }

    public Optional<StringFilter> optionalCondicaoClinica() {
        return Optional.ofNullable(condicaoClinica);
    }

    public StringFilter condicaoClinica() {
        if (condicaoClinica == null) {
            setCondicaoClinica(new StringFilter());
        }
        return condicaoClinica;
    }

    public void setCondicaoClinica(StringFilter condicaoClinica) {
        this.condicaoClinica = condicaoClinica;
    }

    public StringFilter getNomeResponsavel() {
        return nomeResponsavel;
    }

    public Optional<StringFilter> optionalNomeResponsavel() {
        return Optional.ofNullable(nomeResponsavel);
    }

    public StringFilter nomeResponsavel() {
        if (nomeResponsavel == null) {
            setNomeResponsavel(new StringFilter());
        }
        return nomeResponsavel;
    }

    public void setNomeResponsavel(StringFilter nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public StringFilter getCpfResponsavel() {
        return cpfResponsavel;
    }

    public Optional<StringFilter> optionalCpfResponsavel() {
        return Optional.ofNullable(cpfResponsavel);
    }

    public StringFilter cpfResponsavel() {
        if (cpfResponsavel == null) {
            setCpfResponsavel(new StringFilter());
        }
        return cpfResponsavel;
    }

    public void setCpfResponsavel(StringFilter cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public StringFilter getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public Optional<StringFilter> optionalTelefoneResponsavel() {
        return Optional.ofNullable(telefoneResponsavel);
    }

    public StringFilter telefoneResponsavel() {
        if (telefoneResponsavel == null) {
            setTelefoneResponsavel(new StringFilter());
        }
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(StringFilter telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public StringFilter getParentescoResponsavel() {
        return parentescoResponsavel;
    }

    public Optional<StringFilter> optionalParentescoResponsavel() {
        return Optional.ofNullable(parentescoResponsavel);
    }

    public StringFilter parentescoResponsavel() {
        if (parentescoResponsavel == null) {
            setParentescoResponsavel(new StringFilter());
        }
        return parentescoResponsavel;
    }

    public void setParentescoResponsavel(StringFilter parentescoResponsavel) {
        this.parentescoResponsavel = parentescoResponsavel;
    }

    public BooleanFilter getStatusAtivo() {
        return statusAtivo;
    }

    public Optional<BooleanFilter> optionalStatusAtivo() {
        return Optional.ofNullable(statusAtivo);
    }

    public BooleanFilter statusAtivo() {
        if (statusAtivo == null) {
            setStatusAtivo(new BooleanFilter());
        }
        return statusAtivo;
    }

    public void setStatusAtivo(BooleanFilter statusAtivo) {
        this.statusAtivo = statusAtivo;
    }

    public LongFilter getDistribuicoesId() {
        return distribuicoesId;
    }

    public Optional<LongFilter> optionalDistribuicoesId() {
        return Optional.ofNullable(distribuicoesId);
    }

    public LongFilter distribuicoesId() {
        if (distribuicoesId == null) {
            setDistribuicoesId(new LongFilter());
        }
        return distribuicoesId;
    }

    public void setDistribuicoesId(LongFilter distribuicoesId) {
        this.distribuicoesId = distribuicoesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PacienteCriteria that = (PacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(registroHospitalar, that.registroHospitalar) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(pesoNascimento, that.pesoNascimento) &&
            Objects.equals(idadeGestacional, that.idadeGestacional) &&
            Objects.equals(condicaoClinica, that.condicaoClinica) &&
            Objects.equals(nomeResponsavel, that.nomeResponsavel) &&
            Objects.equals(cpfResponsavel, that.cpfResponsavel) &&
            Objects.equals(telefoneResponsavel, that.telefoneResponsavel) &&
            Objects.equals(parentescoResponsavel, that.parentescoResponsavel) &&
            Objects.equals(statusAtivo, that.statusAtivo) &&
            Objects.equals(distribuicoesId, that.distribuicoesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            registroHospitalar,
            dataNascimento,
            pesoNascimento,
            idadeGestacional,
            condicaoClinica,
            nomeResponsavel,
            cpfResponsavel,
            telefoneResponsavel,
            parentescoResponsavel,
            statusAtivo,
            distribuicoesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalRegistroHospitalar().map(f -> "registroHospitalar=" + f + ", ").orElse("") +
            optionalDataNascimento().map(f -> "dataNascimento=" + f + ", ").orElse("") +
            optionalPesoNascimento().map(f -> "pesoNascimento=" + f + ", ").orElse("") +
            optionalIdadeGestacional().map(f -> "idadeGestacional=" + f + ", ").orElse("") +
            optionalCondicaoClinica().map(f -> "condicaoClinica=" + f + ", ").orElse("") +
            optionalNomeResponsavel().map(f -> "nomeResponsavel=" + f + ", ").orElse("") +
            optionalCpfResponsavel().map(f -> "cpfResponsavel=" + f + ", ").orElse("") +
            optionalTelefoneResponsavel().map(f -> "telefoneResponsavel=" + f + ", ").orElse("") +
            optionalParentescoResponsavel().map(f -> "parentescoResponsavel=" + f + ", ").orElse("") +
            optionalStatusAtivo().map(f -> "statusAtivo=" + f + ", ").orElse("") +
            optionalDistribuicoesId().map(f -> "distribuicoesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
