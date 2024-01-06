package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.SchoolStaff;

/**
 * Spring Data JPA repository for the SchoolStaff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolStaffRepository extends JpaRepository<SchoolStaff, Long>, JpaSpecificationExecutor<SchoolStaff> {}
