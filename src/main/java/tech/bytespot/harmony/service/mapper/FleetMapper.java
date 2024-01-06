package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.domain.Schools;
import tech.bytespot.harmony.domain.Terminal;
import tech.bytespot.harmony.service.dto.FleetDTO;
import tech.bytespot.harmony.service.dto.SchoolsDTO;
import tech.bytespot.harmony.service.dto.TerminalDTO;

/**
 * Mapper for the entity {@link Fleet} and its DTO {@link FleetDTO}.
 */
@Mapper(componentModel = "spring")
public interface FleetMapper extends EntityMapper<FleetDTO, Fleet> {
    @Mapping(target = "terminal", source = "terminal")
    @Mapping(target = "school", source = "school")
    FleetDTO toDto(Fleet s);

    @Named("terminalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TerminalDTO toDtoTerminalId(Terminal terminal);

    @Named("schoolsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchoolsDTO toDtoSchoolsId(Schools schools);
}
