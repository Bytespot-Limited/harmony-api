package tech.bytespot.harmony.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.bytespot.harmony.repository.TerminalRepository;
import tech.bytespot.harmony.service.TerminalQueryService;
import tech.bytespot.harmony.service.TerminalService;
import tech.bytespot.harmony.service.criteria.TerminalCriteria;
import tech.bytespot.harmony.service.dto.TerminalDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Terminal}.
 */
@RestController
@RequestMapping("/api")
public class TerminalResource {

    private final Logger log = LoggerFactory.getLogger(TerminalResource.class);

    private static final String ENTITY_NAME = "terminal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TerminalService terminalService;

    private final TerminalRepository terminalRepository;

    private final TerminalQueryService terminalQueryService;

    public TerminalResource(
        TerminalService terminalService,
        TerminalRepository terminalRepository,
        TerminalQueryService terminalQueryService
    ) {
        this.terminalService = terminalService;
        this.terminalRepository = terminalRepository;
        this.terminalQueryService = terminalQueryService;
    }

    /**
     * {@code POST  /terminals} : Create a new terminal.
     *
     * @param terminalDTO the terminalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new terminalDTO, or with status {@code 400 (Bad Request)} if the terminal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/terminals")
    public ResponseEntity<TerminalDTO> createTerminal(@Valid @RequestBody TerminalDTO terminalDTO) throws URISyntaxException {
        log.debug("REST request to save Terminal : {}", terminalDTO);
        if (terminalDTO.getId() != null) {
            throw new BadRequestAlertException("A new terminal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TerminalDTO result = terminalService.save(terminalDTO);
        return ResponseEntity
            .created(new URI("/api/terminals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /terminals/:id} : Updates an existing terminal.
     *
     * @param id the id of the terminalDTO to save.
     * @param terminalDTO the terminalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalDTO,
     * or with status {@code 400 (Bad Request)} if the terminalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the terminalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/terminals/{id}")
    public ResponseEntity<TerminalDTO> updateTerminal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TerminalDTO terminalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Terminal : {}, {}", id, terminalDTO);
        if (terminalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TerminalDTO result = terminalService.update(terminalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, terminalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /terminals/:id} : Partial updates given fields of an existing terminal, field will ignore if it is null
     *
     * @param id the id of the terminalDTO to save.
     * @param terminalDTO the terminalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalDTO,
     * or with status {@code 400 (Bad Request)} if the terminalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the terminalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the terminalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/terminals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TerminalDTO> partialUpdateTerminal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TerminalDTO terminalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Terminal partially : {}, {}", id, terminalDTO);
        if (terminalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TerminalDTO> result = terminalService.partialUpdate(terminalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, terminalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /terminals} : get all the terminals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of terminals in body.
     */
    @GetMapping("/terminals")
    public ResponseEntity<List<TerminalDTO>> getAllTerminals(
        TerminalCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Terminals by criteria: {}", criteria);

        Page<TerminalDTO> page = terminalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /terminals/count} : count all the terminals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/terminals/count")
    public ResponseEntity<Long> countTerminals(TerminalCriteria criteria) {
        log.debug("REST request to count Terminals by criteria: {}", criteria);
        return ResponseEntity.ok().body(terminalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /terminals/:id} : get the "id" terminal.
     *
     * @param id the id of the terminalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the terminalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/terminals/{id}")
    public ResponseEntity<TerminalDTO> getTerminal(@PathVariable Long id) {
        log.debug("REST request to get Terminal : {}", id);
        Optional<TerminalDTO> terminalDTO = terminalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(terminalDTO);
    }

    /**
     * {@code DELETE  /terminals/:id} : delete the "id" terminal.
     *
     * @param id the id of the terminalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/terminals/{id}")
    public ResponseEntity<Void> deleteTerminal(@PathVariable Long id) {
        log.debug("REST request to delete Terminal : {}", id);
        terminalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
