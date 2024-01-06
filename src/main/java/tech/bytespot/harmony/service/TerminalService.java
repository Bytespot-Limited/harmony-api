package tech.bytespot.harmony.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bytespot.harmony.service.dto.TerminalDTO;

/**
 * Service Interface for managing {@link tech.bytespot.harmony.domain.Terminal}.
 */
public interface TerminalService {
    /**
     * Save a terminal.
     *
     * @param terminalDTO the entity to save.
     * @return the persisted entity.
     */
    TerminalDTO save(TerminalDTO terminalDTO);

    /**
     * Updates a terminal.
     *
     * @param terminalDTO the entity to update.
     * @return the persisted entity.
     */
    TerminalDTO update(TerminalDTO terminalDTO);

    /**
     * Partially updates a terminal.
     *
     * @param terminalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TerminalDTO> partialUpdate(TerminalDTO terminalDTO);

    /**
     * Get all the terminals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TerminalDTO> findAll(Pageable pageable);

    /**
     * Get all the TerminalDTO where Fleet is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TerminalDTO> findAllWhereFleetIsNull();

    /**
     * Get the "id" terminal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TerminalDTO> findOne(Long id);

    /**
     * Delete the "id" terminal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
