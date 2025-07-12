package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.LocalPreNatal;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.ResultadoExame;
import com.mycompany.myapp.domain.enumeration.TipoDoadora;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Doadora} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DoadoraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doadoras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoadoraCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoDoadora
     */
    public static class TipoDoadoraFilter extends Filter<TipoDoadora> {

        public TipoDoadoraFilter() {}

        public TipoDoadoraFilter(TipoDoadoraFilter filter) {
            super(filter);
        }

        @Override
        public TipoDoadoraFilter copy() {
            return new TipoDoadoraFilter(this);
        }
    }

    /**
     * Class for filtering LocalPreNatal
     */
    public static class LocalPreNatalFilter extends Filter<LocalPreNatal> {

        public LocalPreNatalFilter() {}

        public LocalPreNatalFilter(LocalPreNatalFilter filter) {
            super(filter);
        }

        @Override
        public LocalPreNatalFilter copy() {
            return new LocalPreNatalFilter(this);
        }
    }

    /**
     * Class for filtering ResultadoExame
     */
    public static class ResultadoExameFilter extends Filter<ResultadoExame> {

        public ResultadoExameFilter() {}

        public ResultadoExameFilter(ResultadoExameFilter filter) {
            super(filter);
        }

        @Override
        public ResultadoExameFilter copy() {
            return new ResultadoExameFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cartaoSUS;

    private StringFilter cpf;

    private LocalDateFilter dataNascimento;

    private StringFilter cep;

    private StringFilter estado;

    private StringFilter cidade;

    private StringFilter endereco;

    private StringFilter telefone;

    private StringFilter profissao;

    private TipoDoadoraFilter tipoDoadora;

    private LocalPreNatalFilter localPreNatal;

    private ResultadoExameFilter resultadoVDRL;

    private ResultadoExameFilter resultadoHBsAg;

    private ResultadoExameFilter resultadoFTAabs;

    private ResultadoExameFilter resultadoHIV;

    private BooleanFilter transfusaoUltimos5Anos;

    private LocalDateFilter dataRegistro;

    private LongFilter coletasId;

    private Boolean distinct;

    public DoadoraCriteria() {}

    public DoadoraCriteria(DoadoraCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.cartaoSUS = other.optionalCartaoSUS().map(StringFilter::copy).orElse(null);
        this.cpf = other.optionalCpf().map(StringFilter::copy).orElse(null);
        this.dataNascimento = other.optionalDataNascimento().map(LocalDateFilter::copy).orElse(null);
        this.cep = other.optionalCep().map(StringFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.cidade = other.optionalCidade().map(StringFilter::copy).orElse(null);
        this.endereco = other.optionalEndereco().map(StringFilter::copy).orElse(null);
        this.telefone = other.optionalTelefone().map(StringFilter::copy).orElse(null);
        this.profissao = other.optionalProfissao().map(StringFilter::copy).orElse(null);
        this.tipoDoadora = other.optionalTipoDoadora().map(TipoDoadoraFilter::copy).orElse(null);
        this.localPreNatal = other.optionalLocalPreNatal().map(LocalPreNatalFilter::copy).orElse(null);
        this.resultadoVDRL = other.optionalResultadoVDRL().map(ResultadoExameFilter::copy).orElse(null);
        this.resultadoHBsAg = other.optionalResultadoHBsAg().map(ResultadoExameFilter::copy).orElse(null);
        this.resultadoFTAabs = other.optionalResultadoFTAabs().map(ResultadoExameFilter::copy).orElse(null);
        this.resultadoHIV = other.optionalResultadoHIV().map(ResultadoExameFilter::copy).orElse(null);
        this.transfusaoUltimos5Anos = other.optionalTransfusaoUltimos5Anos().map(BooleanFilter::copy).orElse(null);
        this.dataRegistro = other.optionalDataRegistro().map(LocalDateFilter::copy).orElse(null);
        this.coletasId = other.optionalColetasId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DoadoraCriteria copy() {
        return new DoadoraCriteria(this);
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

    public StringFilter getCartaoSUS() {
        return cartaoSUS;
    }

    public Optional<StringFilter> optionalCartaoSUS() {
        return Optional.ofNullable(cartaoSUS);
    }

    public StringFilter cartaoSUS() {
        if (cartaoSUS == null) {
            setCartaoSUS(new StringFilter());
        }
        return cartaoSUS;
    }

    public void setCartaoSUS(StringFilter cartaoSUS) {
        this.cartaoSUS = cartaoSUS;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public Optional<StringFilter> optionalCpf() {
        return Optional.ofNullable(cpf);
    }

    public StringFilter cpf() {
        if (cpf == null) {
            setCpf(new StringFilter());
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
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

    public StringFilter getCep() {
        return cep;
    }

    public Optional<StringFilter> optionalCep() {
        return Optional.ofNullable(cep);
    }

    public StringFilter cep() {
        if (cep == null) {
            setCep(new StringFilter());
        }
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public Optional<StringFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public StringFilter estado() {
        if (estado == null) {
            setEstado(new StringFilter());
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public StringFilter getCidade() {
        return cidade;
    }

    public Optional<StringFilter> optionalCidade() {
        return Optional.ofNullable(cidade);
    }

    public StringFilter cidade() {
        if (cidade == null) {
            setCidade(new StringFilter());
        }
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getEndereco() {
        return endereco;
    }

    public Optional<StringFilter> optionalEndereco() {
        return Optional.ofNullable(endereco);
    }

    public StringFilter endereco() {
        if (endereco == null) {
            setEndereco(new StringFilter());
        }
        return endereco;
    }

    public void setEndereco(StringFilter endereco) {
        this.endereco = endereco;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public Optional<StringFilter> optionalTelefone() {
        return Optional.ofNullable(telefone);
    }

    public StringFilter telefone() {
        if (telefone == null) {
            setTelefone(new StringFilter());
        }
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getProfissao() {
        return profissao;
    }

    public Optional<StringFilter> optionalProfissao() {
        return Optional.ofNullable(profissao);
    }

    public StringFilter profissao() {
        if (profissao == null) {
            setProfissao(new StringFilter());
        }
        return profissao;
    }

    public void setProfissao(StringFilter profissao) {
        this.profissao = profissao;
    }

    public TipoDoadoraFilter getTipoDoadora() {
        return tipoDoadora;
    }

    public Optional<TipoDoadoraFilter> optionalTipoDoadora() {
        return Optional.ofNullable(tipoDoadora);
    }

    public TipoDoadoraFilter tipoDoadora() {
        if (tipoDoadora == null) {
            setTipoDoadora(new TipoDoadoraFilter());
        }
        return tipoDoadora;
    }

    public void setTipoDoadora(TipoDoadoraFilter tipoDoadora) {
        this.tipoDoadora = tipoDoadora;
    }

    public LocalPreNatalFilter getLocalPreNatal() {
        return localPreNatal;
    }

    public Optional<LocalPreNatalFilter> optionalLocalPreNatal() {
        return Optional.ofNullable(localPreNatal);
    }

    public LocalPreNatalFilter localPreNatal() {
        if (localPreNatal == null) {
            setLocalPreNatal(new LocalPreNatalFilter());
        }
        return localPreNatal;
    }

    public void setLocalPreNatal(LocalPreNatalFilter localPreNatal) {
        this.localPreNatal = localPreNatal;
    }

    public ResultadoExameFilter getResultadoVDRL() {
        return resultadoVDRL;
    }

    public Optional<ResultadoExameFilter> optionalResultadoVDRL() {
        return Optional.ofNullable(resultadoVDRL);
    }

    public ResultadoExameFilter resultadoVDRL() {
        if (resultadoVDRL == null) {
            setResultadoVDRL(new ResultadoExameFilter());
        }
        return resultadoVDRL;
    }

    public void setResultadoVDRL(ResultadoExameFilter resultadoVDRL) {
        this.resultadoVDRL = resultadoVDRL;
    }

    public ResultadoExameFilter getResultadoHBsAg() {
        return resultadoHBsAg;
    }

    public Optional<ResultadoExameFilter> optionalResultadoHBsAg() {
        return Optional.ofNullable(resultadoHBsAg);
    }

    public ResultadoExameFilter resultadoHBsAg() {
        if (resultadoHBsAg == null) {
            setResultadoHBsAg(new ResultadoExameFilter());
        }
        return resultadoHBsAg;
    }

    public void setResultadoHBsAg(ResultadoExameFilter resultadoHBsAg) {
        this.resultadoHBsAg = resultadoHBsAg;
    }

    public ResultadoExameFilter getResultadoFTAabs() {
        return resultadoFTAabs;
    }

    public Optional<ResultadoExameFilter> optionalResultadoFTAabs() {
        return Optional.ofNullable(resultadoFTAabs);
    }

    public ResultadoExameFilter resultadoFTAabs() {
        if (resultadoFTAabs == null) {
            setResultadoFTAabs(new ResultadoExameFilter());
        }
        return resultadoFTAabs;
    }

    public void setResultadoFTAabs(ResultadoExameFilter resultadoFTAabs) {
        this.resultadoFTAabs = resultadoFTAabs;
    }

    public ResultadoExameFilter getResultadoHIV() {
        return resultadoHIV;
    }

    public Optional<ResultadoExameFilter> optionalResultadoHIV() {
        return Optional.ofNullable(resultadoHIV);
    }

    public ResultadoExameFilter resultadoHIV() {
        if (resultadoHIV == null) {
            setResultadoHIV(new ResultadoExameFilter());
        }
        return resultadoHIV;
    }

    public void setResultadoHIV(ResultadoExameFilter resultadoHIV) {
        this.resultadoHIV = resultadoHIV;
    }

    public BooleanFilter getTransfusaoUltimos5Anos() {
        return transfusaoUltimos5Anos;
    }

    public Optional<BooleanFilter> optionalTransfusaoUltimos5Anos() {
        return Optional.ofNullable(transfusaoUltimos5Anos);
    }

    public BooleanFilter transfusaoUltimos5Anos() {
        if (transfusaoUltimos5Anos == null) {
            setTransfusaoUltimos5Anos(new BooleanFilter());
        }
        return transfusaoUltimos5Anos;
    }

    public void setTransfusaoUltimos5Anos(BooleanFilter transfusaoUltimos5Anos) {
        this.transfusaoUltimos5Anos = transfusaoUltimos5Anos;
    }

    public LocalDateFilter getDataRegistro() {
        return dataRegistro;
    }

    public Optional<LocalDateFilter> optionalDataRegistro() {
        return Optional.ofNullable(dataRegistro);
    }

    public LocalDateFilter dataRegistro() {
        if (dataRegistro == null) {
            setDataRegistro(new LocalDateFilter());
        }
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateFilter dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public LongFilter getColetasId() {
        return coletasId;
    }

    public Optional<LongFilter> optionalColetasId() {
        return Optional.ofNullable(coletasId);
    }

    public LongFilter coletasId() {
        if (coletasId == null) {
            setColetasId(new LongFilter());
        }
        return coletasId;
    }

    public void setColetasId(LongFilter coletasId) {
        this.coletasId = coletasId;
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
        final DoadoraCriteria that = (DoadoraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cartaoSUS, that.cartaoSUS) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(cidade, that.cidade) &&
            Objects.equals(endereco, that.endereco) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(profissao, that.profissao) &&
            Objects.equals(tipoDoadora, that.tipoDoadora) &&
            Objects.equals(localPreNatal, that.localPreNatal) &&
            Objects.equals(resultadoVDRL, that.resultadoVDRL) &&
            Objects.equals(resultadoHBsAg, that.resultadoHBsAg) &&
            Objects.equals(resultadoFTAabs, that.resultadoFTAabs) &&
            Objects.equals(resultadoHIV, that.resultadoHIV) &&
            Objects.equals(transfusaoUltimos5Anos, that.transfusaoUltimos5Anos) &&
            Objects.equals(dataRegistro, that.dataRegistro) &&
            Objects.equals(coletasId, that.coletasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            cartaoSUS,
            cpf,
            dataNascimento,
            cep,
            estado,
            cidade,
            endereco,
            telefone,
            profissao,
            tipoDoadora,
            localPreNatal,
            resultadoVDRL,
            resultadoHBsAg,
            resultadoFTAabs,
            resultadoHIV,
            transfusaoUltimos5Anos,
            dataRegistro,
            coletasId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoadoraCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalCartaoSUS().map(f -> "cartaoSUS=" + f + ", ").orElse("") +
            optionalCpf().map(f -> "cpf=" + f + ", ").orElse("") +
            optionalDataNascimento().map(f -> "dataNascimento=" + f + ", ").orElse("") +
            optionalCep().map(f -> "cep=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalCidade().map(f -> "cidade=" + f + ", ").orElse("") +
            optionalEndereco().map(f -> "endereco=" + f + ", ").orElse("") +
            optionalTelefone().map(f -> "telefone=" + f + ", ").orElse("") +
            optionalProfissao().map(f -> "profissao=" + f + ", ").orElse("") +
            optionalTipoDoadora().map(f -> "tipoDoadora=" + f + ", ").orElse("") +
            optionalLocalPreNatal().map(f -> "localPreNatal=" + f + ", ").orElse("") +
            optionalResultadoVDRL().map(f -> "resultadoVDRL=" + f + ", ").orElse("") +
            optionalResultadoHBsAg().map(f -> "resultadoHBsAg=" + f + ", ").orElse("") +
            optionalResultadoFTAabs().map(f -> "resultadoFTAabs=" + f + ", ").orElse("") +
            optionalResultadoHIV().map(f -> "resultadoHIV=" + f + ", ").orElse("") +
            optionalTransfusaoUltimos5Anos().map(f -> "transfusaoUltimos5Anos=" + f + ", ").orElse("") +
            optionalDataRegistro().map(f -> "dataRegistro=" + f + ", ").orElse("") +
            optionalColetasId().map(f -> "coletasId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
