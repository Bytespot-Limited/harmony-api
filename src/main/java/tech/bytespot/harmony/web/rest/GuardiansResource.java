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
import tech.bytespot.harmony.repository.GuardiansRepository;
import tech.bytespot.harmony.service.GuardiansQueryService;
import tech.bytespot.harmony.service.GuardiansService;
import tech.bytespot.harmony.service.criteria.GuardiansCriteria;
import tech.bytespot.harmony.service.dto.GuardiansDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Guardians}.
 */
@RestController
@RequestMapping("/api")
public class GuardiansResource {

    private final Logger log = LoggerFactory.getLogger(GuardiansResource.class);

    private static final String ENTITY_NAME = "guardians";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuardiansService guardiansService;

    private final GuardiansRepository guardiansRepository;

    private final GuardiansQueryService guardiansQueryService;

    public GuardiansResource(
        GuardiansService guardiansService,
        GuardiansRepository guardiansRepository,
        GuardiansQueryService guardiansQueryService
    ) {
        this.guardiansService = guardiansService;
        this.guardiansRepository = guardiansRepository;
        this.guardiansQueryService = guardiansQueryService;
    }

    /**
     * {@code POST  /guardians} : Create a new guardians.
     *
     * @param guardiansDTO the guardiansDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guardiansDTO, or with status {@code 400 (Bad Request)} if the guardians has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guardians")
    public ResponseEntity<GuardiansDTO> createGuardians(@Valid @RequestBody GuardiansDTO guardiansDTO) throws URISyntaxException {
        log.debug("REST request to save Guardians : {}", guardiansDTO);
        if (guardiansDTO.getId() != null) {
            throw new BadRequestAlertException("A new guardians cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuardiansDTO result = guardiansService.save(guardiansDTO);
        return ResponseEntity
            .created(new URI("/api/guardians/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guardians/:id} : Updates an existing guardians.
     *
     * @param id the id of the guardiansDTO to save.
     * @param guardiansDTO the guardiansDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardiansDTO,
     * or with status {@code 400 (Bad Request)} if the guardiansDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guardiansDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guardians/{id}")
    public ResponseEntity<GuardiansDTO> updateGuardians(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GuardiansDTO guardiansDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Guardians : {}, {}", id, guardiansDTO);
        if (guardiansDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guardiansDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guardiansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GuardiansDTO result = guardiansService.update(guardiansDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, guardiansDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /guardians/:id} : Partial updates given fields of an existing guardians, field will ignore if it is null
     *
     * @param id the id of the guardiansDTO to save.
     * @param guardiansDTO the guardiansDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardiansDTO,
     * or with status {@code 400 (Bad Request)} if the guardiansDTO is not valid,
     * or with status {@code 404 (Not Found)} if the guardiansDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the guardiansDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/guardians/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GuardiansDTO> partialUpdateGuardians(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GuardiansDTO guardiansDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Guardians partially : {}, {}", id, guardiansDTO);
        if (guardiansDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guardiansDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guardiansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GuardiansDTO> result = guardiansService.partialUpdate(guardiansDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, guardiansDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /guardians} : get all the guardians.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guardians in body.
     */
    @GetMapping("/guardians")
    public ResponseEntity<List<GuardiansDTO>> getAllGuardians(
        GuardiansCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Guardians by criteria: {}", criteria);

        Page<GuardiansDTO> page = guardiansQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guardians/count} : count all the guardians.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/guardians/count")
    public ResponseEntity<Long> countGuardians(GuardiansCriteria criteria) {
        log.debug("REST request to count Guardians by criteria: {}", criteria);
        return ResponseEntity.ok().body(guardiansQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /guardians/:id} : get the "id" guardians.
     *
     * @param id the id of the guardiansDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guardiansDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guardians/{id}")
    public ResponseEntity<GuardiansDTO> getGuardians(@PathVariable Long id) {
        log.debug("REST request to get Guardians : {}", id);
        Optional<GuardiansDTO> guardiansDTO = guardiansService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guardiansDTO);
    }

    /**
     * {@code DELETE  /guardians/:id} : delete the "id" guardians.
     *
     * @param id the id of the guardiansDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guardians/{id}")
    public ResponseEntity<Void> deleteGuardians(@PathVariable Long id) {
        log.debug("REST request to delete Guardians : {}", id);
        guardiansService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
