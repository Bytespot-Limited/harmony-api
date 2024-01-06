package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Notifications;

/**
 * Spring Data JPA repository for the Notifications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long>, JpaSpecificationExecutor<Notifications> {}
