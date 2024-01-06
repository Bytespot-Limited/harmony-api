package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.FleetDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Fleet}.
 */
public interface FleetService {
    /**
     * Save a fleet.
     *
     * @param fleetDTO the entity to save.
     * @return the persisted entity.
     */
    FleetDTO save(FleetDTO fleetDTO);

    /**
     * Updates a fleet.
     *
     * @param fleetDTO the entity to update.
     * @return the persisted entity.
     */
    FleetDTO update(FleetDTO fleetDTO);

    /**
     * Partially updates a fleet.
     *
     * @param fleetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FleetDTO> partialUpdate(FleetDTO fleetDTO);

    /**
     * Get all the fleets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FleetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fleet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FleetDTO> findOne(Long id);

    /**
     * Delete the "id" fleet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
