package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.SchoolsDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Schools}.
 */
public interface SchoolsService {
    /**
     * Save a schools.
     *
     * @param schoolsDTO the entity to save.
     * @return the persisted entity.
     */
    SchoolsDTO save(SchoolsDTO schoolsDTO);

    /**
     * Updates a schools.
     *
     * @param schoolsDTO the entity to update.
     * @return the persisted entity.
     */
    SchoolsDTO update(SchoolsDTO schoolsDTO);

    /**
     * Partially updates a schools.
     *
     * @param schoolsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchoolsDTO> partialUpdate(SchoolsDTO schoolsDTO);

    /**
     * Get all the schools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchoolsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schools.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchoolsDTO> findOne(Long id);

    /**
     * Delete the "id" schools.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
