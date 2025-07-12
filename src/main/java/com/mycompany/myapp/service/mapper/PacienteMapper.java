package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {}
