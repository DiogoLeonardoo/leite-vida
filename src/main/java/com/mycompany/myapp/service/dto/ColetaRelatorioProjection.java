package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.StatusColeta;
import java.time.LocalDate;

public interface ColetaRelatorioProjection {
    Long getColetaId();
    LocalDate getDataColeta();
    Double getVolumeMl();
    String getLocalColeta();
    String getStatusColeta();
    String getNomeDoadora();
    String getCpfDoadora();
    String getTelefoneDoadora();
}
