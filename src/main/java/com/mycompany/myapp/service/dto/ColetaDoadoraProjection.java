package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public interface ColetaDoadoraProjection {
    Long getColetaId();
    LocalDate getDataColeta();
    Double getVolumeMl();
    String getLocalColeta();
    String getStatusColeta();
    String getNomeDoadora();
    String getCpfDoadora();
    String getTelefoneDoadora();
}
