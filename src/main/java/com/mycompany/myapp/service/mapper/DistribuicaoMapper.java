package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Distribuicao;
import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.service.dto.DistribuicaoDTO;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Distribuicao} and its DTO {@link DistribuicaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DistribuicaoMapper extends EntityMapper<DistribuicaoDTO, Distribuicao> {
    @Mapping(target = "estoque", source = "estoque", qualifiedByName = "estoqueId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    DistribuicaoDTO toDto(Distribuicao s);

    @Named("estoqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstoqueDTO toDtoEstoqueId(Estoque estoque);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);
}
