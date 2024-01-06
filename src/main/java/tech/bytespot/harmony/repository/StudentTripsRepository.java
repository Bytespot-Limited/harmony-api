package tech.bytespot.harmony.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.bytespot.harmony.domain.StudentTrips;
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.domain.enumeration.BoardingStatus;

/** Spring Data JPA repository for the StudentTrips entity. */
@SuppressWarnings("unused")
@Repository
public interface StudentTripsRepository
    extends JpaRepository<StudentTrips, Long>, JpaSpecificationExecutor<StudentTrips> {
  List<StudentTrips> findAllByTripAndStatusIn(Trips trip, List<BoardingStatus> status);
}
