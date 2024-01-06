package tech.bytespot.harmony.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.Terminal;

/** Spring Data JPA repository for the Terminal entity. */
@SuppressWarnings("unused")
@Repository
public interface TerminalRepository
    extends JpaRepository<Terminal, Long>, JpaSpecificationExecutor<Terminal> {
  Optional<Terminal> findOneByDevideId(String devideId);
}
