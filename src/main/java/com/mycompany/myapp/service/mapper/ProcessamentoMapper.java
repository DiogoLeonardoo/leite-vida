package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.domain.Processamento;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.dto.ProcessamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Processamento} and its DTO {@link ProcessamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessamentoMapper extends EntityMapper<ProcessamentoDTO, Processamento> {
    @Mapping(target = "estoque", source = "estoque", qualifiedByName = "estoqueId")
    ProcessamentoDTO toDto(Processamento s);

    @Named("estoqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstoqueDTO toDtoEstoqueId(Estoque estoque);
}
