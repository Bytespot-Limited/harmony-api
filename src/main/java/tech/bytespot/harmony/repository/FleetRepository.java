package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Fleet;

/**
 * Spring Data JPA repository for the Fleet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FleetRepository extends JpaRepository<Fleet, Long>, JpaSpecificationExecutor<Fleet> {}
