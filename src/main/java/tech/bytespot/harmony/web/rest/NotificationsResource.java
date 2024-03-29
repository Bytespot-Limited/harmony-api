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
import tech.bytespot.harmony.repository.NotificationsRepository;
import tech.bytespot.harmony.service.NotificationsQueryService;
import tech.bytespot.harmony.service.NotificationsService;
import tech.bytespot.harmony.service.criteria.NotificationsCriteria;
import tech.bytespot.harmony.service.dto.NotificationsDTO;
import tech.bytespot.harmony.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/** REST controller for managing {@link tech.bytespot.harmony.domain.Notifications}. */
@RestController
@RequestMapping("/api")
public class NotificationsResource {

  private final Logger log = LoggerFactory.getLogger(NotificationsResource.class);

  private static final String ENTITY_NAME = "notifications";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final NotificationsService notificationsService;

  private final NotificationsRepository notificationsRepository;

  private final NotificationsQueryService notificationsQueryService;

  public NotificationsResource(
      NotificationsService notificationsService,
      NotificationsRepository notificationsRepository,
      NotificationsQueryService notificationsQueryService) {
    this.notificationsService = notificationsService;
    this.notificationsRepository = notificationsRepository;
    this.notificationsQueryService = notificationsQueryService;
  }

  /**
   * {@code POST /notifications} : Create a new notifications.
   *
   * @param notificationsDTO the notificationsDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     notificationsDTO, or with status {@code 400 (Bad Request)} if the notifications has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/notifications")
  public ResponseEntity<NotificationsDTO> createNotifications(
      @Valid @RequestBody NotificationsDTO notificationsDTO) throws URISyntaxException {
    log.debug("REST request to save Notifications : {}", notificationsDTO);
    if (notificationsDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new notifications cannot already have an ID", ENTITY_NAME, "idexists");
    }
    NotificationsDTO result = notificationsService.save(notificationsDTO);
    return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, false, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /notifications/:id} : Updates an existing notifications.
   *
   * @param id the id of the notificationsDTO to save.
   * @param notificationsDTO the notificationsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     notificationsDTO, or with status {@code 400 (Bad Request)} if the notificationsDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the notificationsDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/notifications/{id}")
  public ResponseEntity<NotificationsDTO> updateNotifications(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody NotificationsDTO notificationsDTO)
      throws URISyntaxException {
    log.debug("REST request to update Notifications : {}, {}", id, notificationsDTO);
    if (notificationsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, notificationsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!notificationsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    NotificationsDTO result = notificationsService.update(notificationsDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, false, ENTITY_NAME, notificationsDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code PATCH /notifications/:id} : Partial updates given fields of an existing notifications,
   * field will ignore if it is null
   *
   * @param id the id of the notificationsDTO to save.
   * @param notificationsDTO the notificationsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     notificationsDTO, or with status {@code 400 (Bad Request)} if the notificationsDTO is not
   *     valid, or with status {@code 404 (Not Found)} if the notificationsDTO is not found, or with
   *     status {@code 500 (Internal Server Error)} if the notificationsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(
      value = "/notifications/{id}",
      consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<NotificationsDTO> partialUpdateNotifications(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody NotificationsDTO notificationsDTO)
      throws URISyntaxException {
    log.debug(
        "REST request to partial update Notifications partially : {}, {}", id, notificationsDTO);
    if (notificationsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, notificationsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!notificationsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<NotificationsDTO> result = notificationsService.partialUpdate(notificationsDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(
            applicationName, false, ENTITY_NAME, notificationsDTO.getId().toString()));
  }

  /**
   * {@code GET /notifications} : get all the notifications.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications
   *     in body.
   */
  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationsDTO>> getAllNotifications(
      NotificationsCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get Notifications by criteria: {}", criteria);

    Page<NotificationsDTO> page = notificationsQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers =
        PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET /notifications/count} : count all the notifications.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
   */
  @GetMapping("/notifications/count")
  public ResponseEntity<Long> countNotifications(NotificationsCriteria criteria) {
    log.debug("REST request to count Notifications by criteria: {}", criteria);
    return ResponseEntity.ok().body(notificationsQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET /notifications/:id} : get the "id" notifications.
   *
   * @param id the id of the notificationsDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     notificationsDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/notifications/{id}")
  public ResponseEntity<NotificationsDTO> getNotifications(@PathVariable Long id) {
    log.debug("REST request to get Notifications : {}", id);
    Optional<NotificationsDTO> notificationsDTO = notificationsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(notificationsDTO);
  }

  /**
   * {@code DELETE /notifications/:id} : delete the "id" notifications.
   *
   * @param id the id of the notificationsDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/notifications/{id}")
  public ResponseEntity<Void> deleteNotifications(@PathVariable Long id) {
    log.debug("REST request to delete Notifications : {}", id);
    notificationsService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(
                applicationName, false, ENTITY_NAME, id.toString()))
        .build();
  }

  //@GetMapping
  @PostMapping("/notifications/sms-delivery-report")
  public ResponseEntity<NotificationsDTO> smsDeliveryReport(@RequestBody String smsDeliveryReport)
      throws URISyntaxException {
    log.debug("REST request to receive delivery report : {}", smsDeliveryReport);

    return ResponseEntity.created(new URI("/api/notifications/sms-delivery-report")).build();
  }
}
