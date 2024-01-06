package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.SchoolStaff;
import tech.bytespot.harmony.service.dto.SchoolStaffDTO;

/**
 * Mapper for the entity {@link SchoolStaff} and its DTO {@link SchoolStaffDTO}.
 */
@Mapper(componentModel = "spring")
public interface SchoolStaffMapper extends EntityMapper<SchoolStaffDTO, SchoolStaff> {}
