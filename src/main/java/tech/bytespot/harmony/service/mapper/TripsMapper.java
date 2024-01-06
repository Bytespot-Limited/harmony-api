package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.service.dto.TripsDTO;

/**
 * Mapper for the entity {@link Trips} and its DTO {@link TripsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripsMapper extends EntityMapper<TripsDTO, Trips> {}
