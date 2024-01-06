package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Schools;
import tech.bytespot.harmony.service.dto.SchoolsDTO;

/**
 * Mapper for the entity {@link Schools} and its DTO {@link SchoolsDTO}.
 */
@Mapper(componentModel = "spring")
public interface SchoolsMapper extends EntityMapper<SchoolsDTO, Schools> {}
