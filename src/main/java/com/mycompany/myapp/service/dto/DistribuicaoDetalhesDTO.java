package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ClassificacaoLeite;
import com.mycompany.myapp.domain.enumeration.TipoLeite;

public class DistribuicaoDetalhesDTO {

    private String nomePaciente;
    private String telefoneResponsavel;
    private String cpfResponsavel;
    private String parentescoResponsavel;
    private Long estoqueId;
    private TipoLeite tipoLeite;
    private ClassificacaoLeite classificacao;
    private String nomeDoadora;

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getParentescoResponsavel() {
        return parentescoResponsavel;
    }

    public void setParentescoResponsavel(String parentescoResponsavel) {
        this.parentescoResponsavel = parentescoResponsavel;
    }

    public Long getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(Long estoqueId) {
        this.estoqueId = estoqueId;
    }

    public TipoLeite getTipoLeite() {
        return tipoLeite;
    }

    public void setTipoLeite(TipoLeite tipoLeite) {
        this.tipoLeite = tipoLeite;
    }

    public ClassificacaoLeite getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(ClassificacaoLeite classificacao) {
        this.classificacao = classificacao;
    }

    public String getNomeDoadora() {
        return nomeDoadora;
    }

    public void setNomeDoadora(String nomeDoadora) {
        this.nomeDoadora = nomeDoadora;
    }
}
