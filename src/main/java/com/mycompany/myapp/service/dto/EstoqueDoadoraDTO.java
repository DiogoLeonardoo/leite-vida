package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public record EstoqueDoadoraDTO(
    Long estoqueId,
    LocalDate dataValidade,
    String tipoLeite,
    String classificacao,
    Double volumeDisponivelMl,
    String nomeDoadora,
    String cpfDoadora,
    String telefoneDoadora
) {}
