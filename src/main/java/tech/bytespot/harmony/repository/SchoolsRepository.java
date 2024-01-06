package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Schools;

/**
 * Spring Data JPA repository for the Schools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolsRepository extends JpaRepository<Schools, Long>, JpaSpecificationExecutor<Schools> {}
