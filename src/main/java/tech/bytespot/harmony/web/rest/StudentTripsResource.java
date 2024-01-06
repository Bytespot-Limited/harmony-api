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
import tech.bytespot.harmony.repository.StudentTripsRepository;
import tech.bytespot.harmony.service.StudentTripsQueryService;
import tech.bytespot.harmony.service.StudentTripsService;
import tech.bytespot.harmony.service.criteria.StudentTripsCriteria;
import tech.bytespot.harmony.service.dto.StudentTripsDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.StudentTrips}.
 */
@RestController
@RequestMapping("/api")
public class StudentTripsResource {

    private final Logger log = LoggerFactory.getLogger(StudentTripsResource.class);

    private static final String ENTITY_NAME = "studentTrips";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentTripsService studentTripsService;

    private final StudentTripsRepository studentTripsRepository;

    private final StudentTripsQueryService studentTripsQueryService;

    public StudentTripsResource(
        StudentTripsService studentTripsService,
        StudentTripsRepository studentTripsRepository,
        StudentTripsQueryService studentTripsQueryService
    ) {
        this.studentTripsService = studentTripsService;
        this.studentTripsRepository = studentTripsRepository;
        this.studentTripsQueryService = studentTripsQueryService;
    }

    /**
     * {@code POST  /student-trips} : Create a new studentTrips.
     *
     * @param studentTripsDTO the studentTripsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentTripsDTO, or with status {@code 400 (Bad Request)} if the studentTrips has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-trips")
    public ResponseEntity<StudentTripsDTO> createStudentTrips(@Valid @RequestBody StudentTripsDTO studentTripsDTO)
        throws URISyntaxException {
        log.debug("REST request to save StudentTrips : {}", studentTripsDTO);
        if (studentTripsDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentTrips cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentTripsDTO result = studentTripsService.save(studentTripsDTO);
        return ResponseEntity
            .created(new URI("/api/student-trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-trips/:id} : Updates an existing studentTrips.
     *
     * @param id the id of the studentTripsDTO to save.
     * @param studentTripsDTO the studentTripsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentTripsDTO,
     * or with status {@code 400 (Bad Request)} if the studentTripsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentTripsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-trips/{id}")
    public ResponseEntity<StudentTripsDTO> updateStudentTrips(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentTripsDTO studentTripsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StudentTrips : {}, {}", id, studentTripsDTO);
        if (studentTripsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentTripsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentTripsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentTripsDTO result = studentTripsService.update(studentTripsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentTripsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-trips/:id} : Partial updates given fields of an existing studentTrips, field will ignore if it is null
     *
     * @param id the id of the studentTripsDTO to save.
     * @param studentTripsDTO the studentTripsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentTripsDTO,
     * or with status {@code 400 (Bad Request)} if the studentTripsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentTripsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentTripsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-trips/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentTripsDTO> partialUpdateStudentTrips(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentTripsDTO studentTripsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentTrips partially : {}, {}", id, studentTripsDTO);
        if (studentTripsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentTripsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentTripsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentTripsDTO> result = studentTripsService.partialUpdate(studentTripsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentTripsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-trips} : get all the studentTrips.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentTrips in body.
     */
    @GetMapping("/student-trips")
    public ResponseEntity<List<StudentTripsDTO>> getAllStudentTrips(
        StudentTripsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get StudentTrips by criteria: {}", criteria);

        Page<StudentTripsDTO> page = studentTripsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-trips/count} : count all the studentTrips.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/student-trips/count")
    public ResponseEntity<Long> countStudentTrips(StudentTripsCriteria criteria) {
        log.debug("REST request to count StudentTrips by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentTripsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /student-trips/:id} : get the "id" studentTrips.
     *
     * @param id the id of the studentTripsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentTripsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-trips/{id}")
    public ResponseEntity<StudentTripsDTO> getStudentTrips(@PathVariable Long id) {
        log.debug("REST request to get StudentTrips : {}", id);
        Optional<StudentTripsDTO> studentTripsDTO = studentTripsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentTripsDTO);
    }

    /**
     * {@code DELETE  /student-trips/:id} : delete the "id" studentTrips.
     *
     * @param id the id of the studentTripsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-trips/{id}")
    public ResponseEntity<Void> deleteStudentTrips(@PathVariable Long id) {
        log.debug("REST request to delete StudentTrips : {}", id);
        studentTripsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
