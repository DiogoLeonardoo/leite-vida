package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Coleta;
import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.service.dto.ColetaDTO;
import com.mycompany.myapp.service.dto.DoadoraDTO;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Coleta} and its DTO {@link ColetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ColetaMapper extends EntityMapper<ColetaDTO, Coleta> {
    // Mapear tudo, inclusive doadora completa
    @Mapping(target = "processamento", source = "processamento", qualifiedByName = "processamentoId")
    ColetaDTO toDto(Coleta s);

    @Named("processamentoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProcessamentoDTO toDtoProcessamentoId(Processamento processamento);

    // Se precisar do método parcial para doadora (apenas id), deixe ele aqui
    @Named("doadoraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoadoraDTO toDtoDoadoraId(Doadora doadora);
}
