package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.domain.Guardians;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.service.dto.FleetDTO;
import tech.bytespot.harmony.service.dto.GuardiansDTO;
import tech.bytespot.harmony.service.dto.StudentsDTO;

/**
 * Mapper for the entity {@link Students} and its DTO {@link StudentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentsMapper extends EntityMapper<StudentsDTO, Students> {
    @Mapping(target = "fleet", source = "fleet")
    @Mapping(target = "guardian", source = "guardian")
    StudentsDTO toDto(Students s);

    @Named("fleetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FleetDTO toDtoFleetId(Fleet fleet);

    @Named("guardiansId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GuardiansDTO toDtoGuardiansId(Guardians guardians);
}
