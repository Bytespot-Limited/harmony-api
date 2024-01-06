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
import tech.bytespot.harmony.repository.FleetRepository;
import tech.bytespot.harmony.service.FleetQueryService;
import tech.bytespot.harmony.service.FleetService;
import tech.bytespot.harmony.service.criteria.FleetCriteria;
import tech.bytespot.harmony.service.dto.FleetDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Fleet}.
 */
@RestController
@RequestMapping("/api")
public class FleetResource {

    private final Logger log = LoggerFactory.getLogger(FleetResource.class);

    private static final String ENTITY_NAME = "fleet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FleetService fleetService;

    private final FleetRepository fleetRepository;

    private final FleetQueryService fleetQueryService;

    public FleetResource(FleetService fleetService, FleetRepository fleetRepository, FleetQueryService fleetQueryService) {
        this.fleetService = fleetService;
        this.fleetRepository = fleetRepository;
        this.fleetQueryService = fleetQueryService;
    }

    /**
     * {@code POST  /fleets} : Create a new fleet.
     *
     * @param fleetDTO the fleetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fleetDTO, or with status {@code 400 (Bad Request)} if the fleet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fleets")
    public ResponseEntity<FleetDTO> createFleet(@Valid @RequestBody FleetDTO fleetDTO) throws URISyntaxException {
        log.debug("REST request to save Fleet : {}", fleetDTO);
        if (fleetDTO.getId() != null) {
            throw new BadRequestAlertException("A new fleet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FleetDTO result = fleetService.save(fleetDTO);
        return ResponseEntity
            .created(new URI("/api/fleets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fleets/:id} : Updates an existing fleet.
     *
     * @param id the id of the fleetDTO to save.
     * @param fleetDTO the fleetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fleetDTO,
     * or with status {@code 400 (Bad Request)} if the fleetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fleetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fleets/{id}")
    public ResponseEntity<FleetDTO> updateFleet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FleetDTO fleetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fleet : {}, {}", id, fleetDTO);
        if (fleetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fleetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fleetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FleetDTO result = fleetService.update(fleetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fleetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fleets/:id} : Partial updates given fields of an existing fleet, field will ignore if it is null
     *
     * @param id the id of the fleetDTO to save.
     * @param fleetDTO the fleetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fleetDTO,
     * or with status {@code 400 (Bad Request)} if the fleetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fleetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fleetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fleets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FleetDTO> partialUpdateFleet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FleetDTO fleetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fleet partially : {}, {}", id, fleetDTO);
        if (fleetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fleetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fleetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FleetDTO> result = fleetService.partialUpdate(fleetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fleetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fleets} : get all the fleets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fleets in body.
     */
    @GetMapping("/fleets")
    public ResponseEntity<List<FleetDTO>> getAllFleets(
        FleetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Fleets by criteria: {}", criteria);

        Page<FleetDTO> page = fleetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fleets/count} : count all the fleets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fleets/count")
    public ResponseEntity<Long> countFleets(FleetCriteria criteria) {
        log.debug("REST request to count Fleets by criteria: {}", criteria);
        return ResponseEntity.ok().body(fleetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fleets/:id} : get the "id" fleet.
     *
     * @param id the id of the fleetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fleetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fleets/{id}")
    public ResponseEntity<FleetDTO> getFleet(@PathVariable Long id) {
        log.debug("REST request to get Fleet : {}", id);
        Optional<FleetDTO> fleetDTO = fleetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fleetDTO);
    }

    /**
     * {@code DELETE  /fleets/:id} : delete the "id" fleet.
     *
     * @param id the id of the fleetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fleets/{id}")
    public ResponseEntity<Void> deleteFleet(@PathVariable Long id) {
        log.debug("REST request to delete Fleet : {}", id);
        fleetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
