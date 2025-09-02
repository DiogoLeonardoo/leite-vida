package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public record EstoqueDoadoraDTO(
    Long estoqueId,
    LocalDate dataValidade,
    String tipoLeite,
    String classificacao,
    Double volumeDisponivelMl,
    Double temperaturaArmazenamento,
    String nomeDoadora,
    String cpfDoadora,
    String telefoneDoadora,
    String tecnicoResponsavel,
    LocalDate dataProcessamento,
    Double valorAcidezDornic,
    Double valorCaloricoKcal
) {}
