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
import tech.bytespot.harmony.repository.SchoolStaffRepository;
import tech.bytespot.harmony.service.SchoolStaffQueryService;
import tech.bytespot.harmony.service.SchoolStaffService;
import tech.bytespot.harmony.service.criteria.SchoolStaffCriteria;
import tech.bytespot.harmony.service.dto.SchoolStaffDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.bytespot.harmony.domain.SchoolStaff}.
 */
@RestController
@RequestMapping("/api")
public class SchoolStaffResource {

    private final Logger log = LoggerFactory.getLogger(SchoolStaffResource.class);

    private static final String ENTITY_NAME = "schoolStaff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolStaffService schoolStaffService;

    private final SchoolStaffRepository schoolStaffRepository;

    private final SchoolStaffQueryService schoolStaffQueryService;

    public SchoolStaffResource(
        SchoolStaffService schoolStaffService,
        SchoolStaffRepository schoolStaffRepository,
        SchoolStaffQueryService schoolStaffQueryService
    ) {
        this.schoolStaffService = schoolStaffService;
        this.schoolStaffRepository = schoolStaffRepository;
        this.schoolStaffQueryService = schoolStaffQueryService;
    }

    /**
     * {@code POST  /school-staffs} : Create a new schoolStaff.
     *
     * @param schoolStaffDTO the schoolStaffDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolStaffDTO, or with status {@code 400 (Bad Request)} if the schoolStaff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/school-staffs")
    public ResponseEntity<SchoolStaffDTO> createSchoolStaff(@Valid @RequestBody SchoolStaffDTO schoolStaffDTO) throws URISyntaxException {
        log.debug("REST request to save SchoolStaff : {}", schoolStaffDTO);
        if (schoolStaffDTO.getId() != null) {
            throw new BadRequestAlertException("A new schoolStaff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolStaffDTO result = schoolStaffService.save(schoolStaffDTO);
        return ResponseEntity
            .created(new URI("/api/school-staffs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /school-staffs/:id} : Updates an existing schoolStaff.
     *
     * @param id the id of the schoolStaffDTO to save.
     * @param schoolStaffDTO the schoolStaffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolStaffDTO,
     * or with status {@code 400 (Bad Request)} if the schoolStaffDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolStaffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/school-staffs/{id}")
    public ResponseEntity<SchoolStaffDTO> updateSchoolStaff(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SchoolStaffDTO schoolStaffDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SchoolStaff : {}, {}", id, schoolStaffDTO);
        if (schoolStaffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolStaffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolStaffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchoolStaffDTO result = schoolStaffService.update(schoolStaffDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schoolStaffDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /school-staffs/:id} : Partial updates given fields of an existing schoolStaff, field will ignore if it is null
     *
     * @param id the id of the schoolStaffDTO to save.
     * @param schoolStaffDTO the schoolStaffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolStaffDTO,
     * or with status {@code 400 (Bad Request)} if the schoolStaffDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schoolStaffDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schoolStaffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/school-staffs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchoolStaffDTO> partialUpdateSchoolStaff(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SchoolStaffDTO schoolStaffDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchoolStaff partially : {}, {}", id, schoolStaffDTO);
        if (schoolStaffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolStaffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolStaffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchoolStaffDTO> result = schoolStaffService.partialUpdate(schoolStaffDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schoolStaffDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /school-staffs} : get all the schoolStaffs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schoolStaffs in body.
     */
    @GetMapping("/school-staffs")
    public ResponseEntity<List<SchoolStaffDTO>> getAllSchoolStaffs(
        SchoolStaffCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SchoolStaffs by criteria: {}", criteria);

        Page<SchoolStaffDTO> page = schoolStaffQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /school-staffs/count} : count all the schoolStaffs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/school-staffs/count")
    public ResponseEntity<Long> countSchoolStaffs(SchoolStaffCriteria criteria) {
        log.debug("REST request to count SchoolStaffs by criteria: {}", criteria);
        return ResponseEntity.ok().body(schoolStaffQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /school-staffs/:id} : get the "id" schoolStaff.
     *
     * @param id the id of the schoolStaffDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolStaffDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/school-staffs/{id}")
    public ResponseEntity<SchoolStaffDTO> getSchoolStaff(@PathVariable Long id) {
        log.debug("REST request to get SchoolStaff : {}", id);
        Optional<SchoolStaffDTO> schoolStaffDTO = schoolStaffService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolStaffDTO);
    }

    /**
     * {@code DELETE  /school-staffs/:id} : delete the "id" schoolStaff.
     *
     * @param id the id of the schoolStaffDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/school-staffs/{id}")
    public ResponseEntity<Void> deleteSchoolStaff(@PathVariable Long id) {
        log.debug("REST request to delete SchoolStaff : {}", id);
        schoolStaffService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
