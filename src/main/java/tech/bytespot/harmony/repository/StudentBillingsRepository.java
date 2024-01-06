package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.StudentBillings;

/**
 * Spring Data JPA repository for the StudentBillings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentBillingsRepository extends JpaRepository<StudentBillings, Long>, JpaSpecificationExecutor<StudentBillings> {}
