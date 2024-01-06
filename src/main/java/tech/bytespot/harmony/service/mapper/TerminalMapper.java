package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Terminal;
import tech.bytespot.harmony.service.dto.TerminalDTO;

/**
 * Mapper for the entity {@link Terminal} and its DTO {@link TerminalDTO}.
 */
@Mapper(componentModel = "spring")
public interface TerminalMapper extends EntityMapper<TerminalDTO, Terminal> {}
