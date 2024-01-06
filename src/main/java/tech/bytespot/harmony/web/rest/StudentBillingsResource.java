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
import tech.bytespot.harmony.repository.StudentBillingsRepository;
import tech.bytespot.harmony.service.StudentBillingsQueryService;
import tech.bytespot.harmony.service.StudentBillingsService;
import tech.bytespot.harmony.service.criteria.StudentBillingsCriteria;
import tech.bytespot.harmony.service.dto.StudentBillingsDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.StudentBillings}.
 */
@RestController
@RequestMapping("/api")
public class StudentBillingsResource {

    private final Logger log = LoggerFactory.getLogger(StudentBillingsResource.class);

    private static final String ENTITY_NAME = "studentBillings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentBillingsService studentBillingsService;

    private final StudentBillingsRepository studentBillingsRepository;

    private final StudentBillingsQueryService studentBillingsQueryService;

    public StudentBillingsResource(
        StudentBillingsService studentBillingsService,
        StudentBillingsRepository studentBillingsRepository,
        StudentBillingsQueryService studentBillingsQueryService
    ) {
        this.studentBillingsService = studentBillingsService;
        this.studentBillingsRepository = studentBillingsRepository;
        this.studentBillingsQueryService = studentBillingsQueryService;
    }

    /**
     * {@code POST  /student-billings} : Create a new studentBillings.
     *
     * @param studentBillingsDTO the studentBillingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentBillingsDTO, or with status {@code 400 (Bad Request)} if the studentBillings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-billings")
    public ResponseEntity<StudentBillingsDTO> createStudentBillings(@Valid @RequestBody StudentBillingsDTO studentBillingsDTO)
        throws URISyntaxException {
        log.debug("REST request to save StudentBillings : {}", studentBillingsDTO);
        if (studentBillingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentBillings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentBillingsDTO result = studentBillingsService.save(studentBillingsDTO);
        return ResponseEntity
            .created(new URI("/api/student-billings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-billings/:id} : Updates an existing studentBillings.
     *
     * @param id the id of the studentBillingsDTO to save.
     * @param studentBillingsDTO the studentBillingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentBillingsDTO,
     * or with status {@code 400 (Bad Request)} if the studentBillingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentBillingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-billings/{id}")
    public ResponseEntity<StudentBillingsDTO> updateStudentBillings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentBillingsDTO studentBillingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StudentBillings : {}, {}", id, studentBillingsDTO);
        if (studentBillingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentBillingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentBillingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentBillingsDTO result = studentBillingsService.update(studentBillingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentBillingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-billings/:id} : Partial updates given fields of an existing studentBillings, field will ignore if it is null
     *
     * @param id the id of the studentBillingsDTO to save.
     * @param studentBillingsDTO the studentBillingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentBillingsDTO,
     * or with status {@code 400 (Bad Request)} if the studentBillingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentBillingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentBillingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-billings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentBillingsDTO> partialUpdateStudentBillings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentBillingsDTO studentBillingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentBillings partially : {}, {}", id, studentBillingsDTO);
        if (studentBillingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentBillingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentBillingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentBillingsDTO> result = studentBillingsService.partialUpdate(studentBillingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentBillingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-billings} : get all the studentBillings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentBillings in body.
     */
    @GetMapping("/student-billings")
    public ResponseEntity<List<StudentBillingsDTO>> getAllStudentBillings(
        StudentBillingsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get StudentBillings by criteria: {}", criteria);

        Page<StudentBillingsDTO> page = studentBillingsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-billings/count} : count all the studentBillings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/student-billings/count")
    public ResponseEntity<Long> countStudentBillings(StudentBillingsCriteria criteria) {
        log.debug("REST request to count StudentBillings by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentBillingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /student-billings/:id} : get the "id" studentBillings.
     *
     * @param id the id of the studentBillingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentBillingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-billings/{id}")
    public ResponseEntity<StudentBillingsDTO> getStudentBillings(@PathVariable Long id) {
        log.debug("REST request to get StudentBillings : {}", id);
        Optional<StudentBillingsDTO> studentBillingsDTO = studentBillingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentBillingsDTO);
    }

    /**
     * {@code DELETE  /student-billings/:id} : delete the "id" studentBillings.
     *
     * @param id the id of the studentBillingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-billings/{id}")
    public ResponseEntity<Void> deleteStudentBillings(@PathVariable Long id) {
        log.debug("REST request to delete StudentBillings : {}", id);
        studentBillingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
