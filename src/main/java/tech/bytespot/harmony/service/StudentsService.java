package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.StudentsDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Students}.
 */
public interface StudentsService {
    /**
     * Save a students.
     *
     * @param studentsDTO the entity to save.
     * @return the persisted entity.
     */
    StudentsDTO save(StudentsDTO studentsDTO);

    /**
     * Updates a students.
     *
     * @param studentsDTO the entity to update.
     * @return the persisted entity.
     */
    StudentsDTO update(StudentsDTO studentsDTO);

    /**
     * Partially updates a students.
     *
     * @param studentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentsDTO> partialUpdate(StudentsDTO studentsDTO);

    /**
     * Get all the students.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" students.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentsDTO> findOne(Long id);

    /**
     * Delete the "id" students.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
