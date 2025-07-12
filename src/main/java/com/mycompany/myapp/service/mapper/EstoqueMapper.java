package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Estoque;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estoque} and its DTO {@link EstoqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstoqueMapper extends EntityMapper<EstoqueDTO, Estoque> {}
