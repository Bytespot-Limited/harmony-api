package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.DriversDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Drivers}.
 */
public interface DriversService {
    /**
     * Save a drivers.
     *
     * @param driversDTO the entity to save.
     * @return the persisted entity.
     */
    DriversDTO save(DriversDTO driversDTO);

    /**
     * Updates a drivers.
     *
     * @param driversDTO the entity to update.
     * @return the persisted entity.
     */
    DriversDTO update(DriversDTO driversDTO);

    /**
     * Partially updates a drivers.
     *
     * @param driversDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DriversDTO> partialUpdate(DriversDTO driversDTO);

    /**
     * Get all the drivers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DriversDTO> findAll(Pageable pageable);

    /**
     * Get the "id" drivers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DriversDTO> findOne(Long id);

    /**
     * Delete the "id" drivers.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
