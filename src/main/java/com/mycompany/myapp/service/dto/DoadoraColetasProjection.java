package com.mycompany.myapp.service.dto;

import java.time.LocalDate;

public interface DoadoraColetasProjection {
    Long getColetaId();
    LocalDate getDataColeta();
    Integer getVolumeMl();
    String getLocalColeta();
    String getStatusColeta();
    String getNomeDoadora();
    String getCpfDoadora();
    String getTelefoneDoadora();
}
