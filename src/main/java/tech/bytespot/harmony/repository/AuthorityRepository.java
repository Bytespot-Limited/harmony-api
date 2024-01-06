package tech.bytespot.harmony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bytespot.harmony.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
