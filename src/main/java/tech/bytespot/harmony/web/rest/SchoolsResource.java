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
import tech.bytespot.harmony.repository.SchoolsRepository;
import tech.bytespot.harmony.service.SchoolsQueryService;
import tech.bytespot.harmony.service.SchoolsService;
import tech.bytespot.harmony.service.criteria.SchoolsCriteria;
import tech.bytespot.harmony.service.dto.SchoolsDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Schools}.
 */
@RestController
@RequestMapping("/api")
public class SchoolsResource {

    private final Logger log = LoggerFactory.getLogger(SchoolsResource.class);

    private static final String ENTITY_NAME = "schools";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolsService schoolsService;

    private final SchoolsRepository schoolsRepository;

    private final SchoolsQueryService schoolsQueryService;

    public SchoolsResource(SchoolsService schoolsService, SchoolsRepository schoolsRepository, SchoolsQueryService schoolsQueryService) {
        this.schoolsService = schoolsService;
        this.schoolsRepository = schoolsRepository;
        this.schoolsQueryService = schoolsQueryService;
    }

    /**
     * {@code POST  /schools} : Create a new schools.
     *
     * @param schoolsDTO the schoolsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolsDTO, or with status {@code 400 (Bad Request)} if the schools has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schools")
    public ResponseEntity<SchoolsDTO> createSchools(@Valid @RequestBody SchoolsDTO schoolsDTO) throws URISyntaxException {
        log.debug("REST request to save Schools : {}", schoolsDTO);
        if (schoolsDTO.getId() != null) {
            throw new BadRequestAlertException("A new schools cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolsDTO result = schoolsService.save(schoolsDTO);
        return ResponseEntity
            .created(new URI("/api/schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schools/:id} : Updates an existing schools.
     *
     * @param id the id of the schoolsDTO to save.
     * @param schoolsDTO the schoolsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolsDTO,
     * or with status {@code 400 (Bad Request)} if the schoolsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schools/{id}")
    public ResponseEntity<SchoolsDTO> updateSchools(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SchoolsDTO schoolsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Schools : {}, {}", id, schoolsDTO);
        if (schoolsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchoolsDTO result = schoolsService.update(schoolsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schoolsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schools/:id} : Partial updates given fields of an existing schools, field will ignore if it is null
     *
     * @param id the id of the schoolsDTO to save.
     * @param schoolsDTO the schoolsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolsDTO,
     * or with status {@code 400 (Bad Request)} if the schoolsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schoolsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schoolsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchoolsDTO> partialUpdateSchools(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SchoolsDTO schoolsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Schools partially : {}, {}", id, schoolsDTO);
        if (schoolsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchoolsDTO> result = schoolsService.partialUpdate(schoolsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schoolsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /schools} : get all the schools.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schools in body.
     */
    @GetMapping("/schools")
    public ResponseEntity<List<SchoolsDTO>> getAllSchools(
        SchoolsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Schools by criteria: {}", criteria);

        Page<SchoolsDTO> page = schoolsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schools/count} : count all the schools.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/schools/count")
    public ResponseEntity<Long> countSchools(SchoolsCriteria criteria) {
        log.debug("REST request to count Schools by criteria: {}", criteria);
        return ResponseEntity.ok().body(schoolsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /schools/:id} : get the "id" schools.
     *
     * @param id the id of the schoolsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schools/{id}")
    public ResponseEntity<SchoolsDTO> getSchools(@PathVariable Long id) {
        log.debug("REST request to get Schools : {}", id);
        Optional<SchoolsDTO> schoolsDTO = schoolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolsDTO);
    }

    /**
     * {@code DELETE  /schools/:id} : delete the "id" schools.
     *
     * @param id the id of the schoolsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schools/{id}")
    public ResponseEntity<Void> deleteSchools(@PathVariable Long id) {
        log.debug("REST request to delete Schools : {}", id);
        schoolsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
