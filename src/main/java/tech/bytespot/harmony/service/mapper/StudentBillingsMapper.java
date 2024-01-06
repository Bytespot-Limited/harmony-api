package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.StudentBillings;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.service.dto.StudentBillingsDTO;
import tech.bytespot.harmony.service.dto.StudentsDTO;

/**
 * Mapper for the entity {@link StudentBillings} and its DTO {@link StudentBillingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentBillingsMapper extends EntityMapper<StudentBillingsDTO, StudentBillings> {
    @Mapping(target = "student", source = "student", qualifiedByName = "studentsId")
    StudentBillingsDTO toDto(StudentBillings s);

    @Named("studentsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentsDTO toDtoStudentsId(Students students);
}
