package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Guardians;

/**
 * Spring Data JPA repository for the Guardians entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardiansRepository extends JpaRepository<Guardians, Long>, JpaSpecificationExecutor<Guardians> {}
