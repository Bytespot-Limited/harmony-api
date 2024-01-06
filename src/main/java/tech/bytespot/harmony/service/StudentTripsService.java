package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.StudentTripsDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.StudentTrips}.
 */
public interface StudentTripsService {
    /**
     * Save a studentTrips.
     *
     * @param studentTripsDTO the entity to save.
     * @return the persisted entity.
     */
    StudentTripsDTO save(StudentTripsDTO studentTripsDTO);

    /**
     * Updates a studentTrips.
     *
     * @param studentTripsDTO the entity to update.
     * @return the persisted entity.
     */
    StudentTripsDTO update(StudentTripsDTO studentTripsDTO);

    /**
     * Partially updates a studentTrips.
     *
     * @param studentTripsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentTripsDTO> partialUpdate(StudentTripsDTO studentTripsDTO);

    /**
     * Get all the studentTrips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentTripsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" studentTrips.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentTripsDTO> findOne(Long id);

    /**
     * Delete the "id" studentTrips.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

}
