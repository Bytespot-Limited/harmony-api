package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Drivers;

/**
 * Spring Data JPA repository for the Drivers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriversRepository extends JpaRepository<Drivers, Long>, JpaSpecificationExecutor<Drivers> {}
