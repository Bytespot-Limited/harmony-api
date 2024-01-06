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
import tech.bytespot.harmony.repository.DriversRepository;
import tech.bytespot.harmony.service.DriversQueryService;
import tech.bytespot.harmony.service.DriversService;
import tech.bytespot.harmony.service.criteria.DriversCriteria;
import tech.bytespot.harmony.service.dto.DriversDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Drivers}.
 */
@RestController
@RequestMapping("/api")
public class DriversResource {

    private final Logger log = LoggerFactory.getLogger(DriversResource.class);

    private static final String ENTITY_NAME = "drivers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DriversService driversService;

    private final DriversRepository driversRepository;

    private final DriversQueryService driversQueryService;

    public DriversResource(DriversService driversService, DriversRepository driversRepository, DriversQueryService driversQueryService) {
        this.driversService = driversService;
        this.driversRepository = driversRepository;
        this.driversQueryService = driversQueryService;
    }

    /**
     * {@code POST  /drivers} : Create a new drivers.
     *
     * @param driversDTO the driversDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new driversDTO, or with status {@code 400 (Bad Request)} if the drivers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/drivers")
    public ResponseEntity<DriversDTO> createDrivers(@Valid @RequestBody DriversDTO driversDTO) throws URISyntaxException {
        log.debug("REST request to save Drivers : {}", driversDTO);
        if (driversDTO.getId() != null) {
            throw new BadRequestAlertException("A new drivers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DriversDTO result = driversService.save(driversDTO);
        return ResponseEntity
            .created(new URI("/api/drivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /drivers/:id} : Updates an existing drivers.
     *
     * @param id the id of the driversDTO to save.
     * @param driversDTO the driversDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driversDTO,
     * or with status {@code 400 (Bad Request)} if the driversDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the driversDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/drivers/{id}")
    public ResponseEntity<DriversDTO> updateDrivers(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DriversDTO driversDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Drivers : {}, {}", id, driversDTO);
        if (driversDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driversDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driversRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DriversDTO result = driversService.update(driversDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, driversDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /drivers/:id} : Partial updates given fields of an existing drivers, field will ignore if it is null
     *
     * @param id the id of the driversDTO to save.
     * @param driversDTO the driversDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driversDTO,
     * or with status {@code 400 (Bad Request)} if the driversDTO is not valid,
     * or with status {@code 404 (Not Found)} if the driversDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the driversDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/drivers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DriversDTO> partialUpdateDrivers(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DriversDTO driversDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Drivers partially : {}, {}", id, driversDTO);
        if (driversDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driversDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driversRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DriversDTO> result = driversService.partialUpdate(driversDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, driversDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /drivers} : get all the drivers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of drivers in body.
     */
    @GetMapping("/drivers")
    public ResponseEntity<List<DriversDTO>> getAllDrivers(
        DriversCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Drivers by criteria: {}", criteria);

        Page<DriversDTO> page = driversQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /drivers/count} : count all the drivers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/drivers/count")
    public ResponseEntity<Long> countDrivers(DriversCriteria criteria) {
        log.debug("REST request to count Drivers by criteria: {}", criteria);
        return ResponseEntity.ok().body(driversQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /drivers/:id} : get the "id" drivers.
     *
     * @param id the id of the driversDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the driversDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/drivers/{id}")
    public ResponseEntity<DriversDTO> getDrivers(@PathVariable Long id) {
        log.debug("REST request to get Drivers : {}", id);
        Optional<DriversDTO> driversDTO = driversService.findOne(id);
        return ResponseUtil.wrapOrNotFound(driversDTO);
    }

    /**
     * {@code DELETE  /drivers/:id} : delete the "id" drivers.
     *
     * @param id the id of the driversDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<Void> deleteDrivers(@PathVariable Long id) {
        log.debug("REST request to delete Drivers : {}", id);
        driversService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
