package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.StudentTrips;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.service.dto.StudentTripsDTO;
import tech.bytespot.harmony.service.dto.StudentsDTO;
import tech.bytespot.harmony.service.dto.TripsDTO;

/**
 * Mapper for the entity {@link StudentTrips} and its DTO {@link StudentTripsDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentTripsMapper extends EntityMapper<StudentTripsDTO, StudentTrips> {
    @Mapping(target = "student", source = "student")
    @Mapping(target = "trip", source = "trip")
    StudentTripsDTO toDto(StudentTrips s);

    @Named("studentsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentsDTO toDtoStudentsId(Students students);

    @Named("tripsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripsDTO toDtoTripsId(Trips trips);
}
