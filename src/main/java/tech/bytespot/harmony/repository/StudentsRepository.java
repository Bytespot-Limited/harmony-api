package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Students;

/**
 * Spring Data JPA repository for the Students entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentsRepository extends JpaRepository<Students, Long>, JpaSpecificationExecutor<Students> {}
