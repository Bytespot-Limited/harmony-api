package tech.bytespot.harmony.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.SchoolStaffDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.SchoolStaff}.
 */
public interface SchoolStaffService {
    /**
     * Save a schoolStaff.
     *
     * @param schoolStaffDTO the entity to save.
     * @return the persisted entity.
     */
    SchoolStaffDTO save(SchoolStaffDTO schoolStaffDTO);

    /**
     * Updates a schoolStaff.
     *
     * @param schoolStaffDTO the entity to update.
     * @return the persisted entity.
     */
    SchoolStaffDTO update(SchoolStaffDTO schoolStaffDTO);

    /**
     * Partially updates a schoolStaff.
     *
     * @param schoolStaffDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SchoolStaffDTO> partialUpdate(SchoolStaffDTO schoolStaffDTO);

    /**
     * Get all the schoolStaffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SchoolStaffDTO> findAll(Pageable pageable);

    /**
     * Get the "id" schoolStaff.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchoolStaffDTO> findOne(Long id);

    /**
     * Delete the "id" schoolStaff.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
