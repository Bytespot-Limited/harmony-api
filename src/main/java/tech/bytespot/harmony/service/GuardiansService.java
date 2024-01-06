package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.GuardiansDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Guardians}.
 */
public interface GuardiansService {
    /**
     * Save a guardians.
     *
     * @param guardiansDTO the entity to save.
     * @return the persisted entity.
     */
    GuardiansDTO save(GuardiansDTO guardiansDTO);

    /**
     * Updates a guardians.
     *
     * @param guardiansDTO the entity to update.
     * @return the persisted entity.
     */
    GuardiansDTO update(GuardiansDTO guardiansDTO);

    /**
     * Partially updates a guardians.
     *
     * @param guardiansDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GuardiansDTO> partialUpdate(GuardiansDTO guardiansDTO);

    /**
     * Get all the guardians.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuardiansDTO> findAll(Pageable pageable);

    /**
     * Get the "id" guardians.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GuardiansDTO> findOne(Long id);

    /**
     * Delete the "id" guardians.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
