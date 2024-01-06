package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Drivers;
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.service.dto.DriversDTO;
import tech.bytespot.harmony.service.dto.FleetDTO;

/**
 * Mapper for the entity {@link Drivers} and its DTO {@link DriversDTO}.
 */
@Mapper(componentModel = "spring")
public interface DriversMapper extends EntityMapper<DriversDTO, Drivers> {
    @Mapping(target = "fleet", source = "fleet", qualifiedByName = "fleetId")
    DriversDTO toDto(Drivers s);

    @Named("fleetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FleetDTO toDtoFleetId(Fleet fleet);
}
