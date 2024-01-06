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
import tech.bytespot.harmony.repository.StudentsRepository;
import tech.bytespot.harmony.service.StudentsQueryService;
import tech.bytespot.harmony.service.StudentsService;
import tech.bytespot.harmony.service.criteria.StudentsCriteria;
import tech.bytespot.harmony.service.dto.StudentsDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.Students}.
 */
@RestController
@RequestMapping("/api")
public class StudentsResource {

    private final Logger log = LoggerFactory.getLogger(StudentsResource.class);

    private static final String ENTITY_NAME = "students";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentsService studentsService;

    private final StudentsRepository studentsRepository;

    private final StudentsQueryService studentsQueryService;

    public StudentsResource(
        StudentsService studentsService,
        StudentsRepository studentsRepository,
        StudentsQueryService studentsQueryService
    ) {
        this.studentsService = studentsService;
        this.studentsRepository = studentsRepository;
        this.studentsQueryService = studentsQueryService;
    }

    /**
     * {@code POST  /students} : Create a new students.
     *
     * @param studentsDTO the studentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentsDTO, or with status {@code 400 (Bad Request)} if the students has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/students")
    public ResponseEntity<StudentsDTO> createStudents(@Valid @RequestBody StudentsDTO studentsDTO) throws URISyntaxException {
        log.debug("REST request to save Students : {}", studentsDTO);
        if (studentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new students cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentsDTO result = studentsService.save(studentsDTO);
        return ResponseEntity
            .created(new URI("/api/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /students/:id} : Updates an existing students.
     *
     * @param id the id of the studentsDTO to save.
     * @param studentsDTO the studentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentsDTO,
     * or with status {@code 400 (Bad Request)} if the studentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentsDTO> updateStudents(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentsDTO studentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Students : {}, {}", id, studentsDTO);
        if (studentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentsDTO result = studentsService.update(studentsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /students/:id} : Partial updates given fields of an existing students, field will ignore if it is null
     *
     * @param id the id of the studentsDTO to save.
     * @param studentsDTO the studentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentsDTO,
     * or with status {@code 400 (Bad Request)} if the studentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/students/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentsDTO> partialUpdateStudents(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentsDTO studentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Students partially : {}, {}", id, studentsDTO);
        if (studentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentsDTO> result = studentsService.partialUpdate(studentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /students} : get all the students.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of students in body.
     */
    @GetMapping("/students")
    public ResponseEntity<List<StudentsDTO>> getAllStudents(
        StudentsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Students by criteria: {}", criteria);

        Page<StudentsDTO> page = studentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /students/count} : count all the students.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/students/count")
    public ResponseEntity<Long> countStudents(StudentsCriteria criteria) {
        log.debug("REST request to count Students by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /students/:id} : get the "id" students.
     *
     * @param id the id of the studentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentsDTO> getStudents(@PathVariable Long id) {
        log.debug("REST request to get Students : {}", id);
        Optional<StudentsDTO> studentsDTO = studentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentsDTO);
    }

    /**
     * {@code DELETE  /students/:id} : delete the "id" students.
     *
     * @param id the id of the studentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudents(@PathVariable Long id) {
        log.debug("REST request to delete Students : {}", id);
        studentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
