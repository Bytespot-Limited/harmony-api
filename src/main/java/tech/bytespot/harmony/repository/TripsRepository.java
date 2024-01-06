package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Trips;

/**
 * Spring Data JPA repository for the Trips entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripsRepository extends JpaRepository<Trips, Long>, JpaSpecificationExecutor<Trips> {}
