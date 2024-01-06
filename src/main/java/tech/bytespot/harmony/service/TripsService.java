package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.TripsDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Trips}.
 */
public interface TripsService {
    /**
     * Save a trips.
     *
     * @param tripsDTO the entity to save.
     * @return the persisted entity.
     */
    TripsDTO save(TripsDTO tripsDTO);

    /**
     * Updates a trips.
     *
     * @param tripsDTO the entity to update.
     * @return the persisted entity.
     */
    TripsDTO update(TripsDTO tripsDTO);

    /**
     * Partially updates a trips.
     *
     * @param tripsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TripsDTO> partialUpdate(TripsDTO tripsDTO);

    /**
     * Get all the trips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" trips.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TripsDTO> findOne(Long id);

    /**
     * Delete the "id" trips.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
