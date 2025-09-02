package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public interface ProcessamentoProjection {
    Long getIdProcessamento();
    LocalDate getDataProcessamento();
    String getTecnicoResponsavel();
    Double getValorAcidezDornic();
    Double getValorCaloricoKcal();
    String getResultadoAnalise();
    String getStatusProcessamento();
    Long getColetaId();
}
