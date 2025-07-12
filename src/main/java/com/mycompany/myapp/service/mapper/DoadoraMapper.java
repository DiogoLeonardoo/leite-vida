package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Doadora;
import com.mycompany.myapp.service.dto.DoadoraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doadora} and its DTO {@link DoadoraDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoadoraMapper extends EntityMapper<DoadoraDTO, Doadora> {}
