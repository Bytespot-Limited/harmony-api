package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Guardians;
import tech.bytespot.harmony.service.dto.GuardiansDTO;

/**
 * Mapper for the entity {@link Guardians} and its DTO {@link GuardiansDTO}.
 */
@Mapper(componentModel = "spring")
public interface GuardiansMapper extends EntityMapper<GuardiansDTO, Guardians> {}
