package tech.bytespot.harmony.service.mapper;

import org.mapstruct.*;
import tech.bytespot.harmony.domain.Notifications;
import tech.bytespot.harmony.service.dto.NotificationsDTO;

/**
 * Mapper for the entity {@link Notifications} and its DTO {@link NotificationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationsMapper extends EntityMapper<NotificationsDTO, Notifications> {}
