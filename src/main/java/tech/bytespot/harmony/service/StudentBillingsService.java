package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.StudentBillingsDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.StudentBillings}.
 */
public interface StudentBillingsService {
    /**
     * Save a studentBillings.
     *
     * @param studentBillingsDTO the entity to save.
     * @return the persisted entity.
     */
    StudentBillingsDTO save(StudentBillingsDTO studentBillingsDTO);

    /**
     * Updates a studentBillings.
     *
     * @param studentBillingsDTO the entity to update.
     * @return the persisted entity.
     */
    StudentBillingsDTO update(StudentBillingsDTO studentBillingsDTO);

    /**
     * Partially updates a studentBillings.
     *
     * @param studentBillingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentBillingsDTO> partialUpdate(StudentBillingsDTO studentBillingsDTO);

    /**
     * Get all the studentBillings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentBillingsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" studentBillings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentBillingsDTO> findOne(Long id);

    /**
     * Delete the "id" studentBillings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
