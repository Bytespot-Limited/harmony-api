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
import tech.bytespot.harmony.repository.TripsRepository;
import tech.bytespot.harmony.service.TripsQueryService;
import tech.bytespot.harmony.service.TripsService;
import tech.bytespot.harmony.service.criteria.TripsCriteria;
import tech.bytespot.harmony.service.dto.TripsDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Trips}.
 */
@RestController
@RequestMapping("/api")
public class TripsResource {

    private final Logger log = LoggerFactory.getLogger(TripsResource.class);

    private static final String ENTITY_NAME = "trips";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripsService tripsService;

    private final TripsRepository tripsRepository;

    private final TripsQueryService tripsQueryService;

    public TripsResource(TripsService tripsService, TripsRepository tripsRepository, TripsQueryService tripsQueryService) {
        this.tripsService = tripsService;
        this.tripsRepository = tripsRepository;
        this.tripsQueryService = tripsQueryService;
    }

    /**
     * {@code POST  /trips} : Create a new trips.
     *
     * @param tripsDTO the tripsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripsDTO, or with status {@code 400 (Bad Request)} if the trips has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<TripsDTO> createTrips(@Valid @RequestBody TripsDTO tripsDTO) throws URISyntaxException {
        log.debug("REST request to save Trips : {}", tripsDTO);
        if (tripsDTO.getId() != null) {
            throw new BadRequestAlertException("A new trips cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripsDTO result = tripsService.save(tripsDTO);
        return ResponseEntity
            .created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trips/:id} : Updates an existing trips.
     *
     * @param id the id of the tripsDTO to save.
     * @param tripsDTO the tripsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripsDTO,
     * or with status {@code 400 (Bad Request)} if the tripsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trips/{id}")
    public ResponseEntity<TripsDTO> updateTrips(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TripsDTO tripsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Trips : {}, {}", id, tripsDTO);
        if (tripsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripsDTO result = tripsService.update(tripsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trips/:id} : Partial updates given fields of an existing trips, field will ignore if it is null
     *
     * @param id the id of the tripsDTO to save.
     * @param tripsDTO the tripsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripsDTO,
     * or with status {@code 400 (Bad Request)} if the tripsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trips/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripsDTO> partialUpdateTrips(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TripsDTO tripsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trips partially : {}, {}", id, tripsDTO);
        if (tripsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripsDTO> result = tripsService.partialUpdate(tripsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trips in body.
     */
    @GetMapping("/trips")
    public ResponseEntity<List<TripsDTO>> getAllTrips(
        TripsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Trips by criteria: {}", criteria);

        Page<TripsDTO> page = tripsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trips/count} : count all the trips.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/trips/count")
    public ResponseEntity<Long> countTrips(TripsCriteria criteria) {
        log.debug("REST request to count Trips by criteria: {}", criteria);
        return ResponseEntity.ok().body(tripsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trips.
     *
     * @param id the id of the tripsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<TripsDTO> getTrips(@PathVariable Long id) {
        log.debug("REST request to get Trips : {}", id);
        Optional<TripsDTO> tripsDTO = tripsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripsDTO);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trips.
     *
     * @param id the id of the tripsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<Void> deleteTrips(@PathVariable Long id) {
        log.debug("REST request to delete Trips : {}", id);
        tripsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
