package tech.bytespot.harmony.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.IntegrationTest;
import tech.bytespot.harmony.domain.StudentTrips;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.domain.enumeration.BoardingStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.repository.StudentTripsRepository;
import tech.bytespot.harmony.service.dto.StudentTripsDTO;
import tech.bytespot.harmony.service.mapper.StudentTripsMapper;

/**
 * Integration tests for the {@link StudentTripsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentTripsResourceIT {

    private static final BoardingStatus DEFAULT_STATUS = BoardingStatus.BOARDED;
    private static final BoardingStatus UPDATED_STATUS = BoardingStatus.DROPPED_OFF;

    private static final Instant DEFAULT_PICKUP_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PICKUP_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DROP_OFF_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DROP_OFF_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/student-trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentTripsRepository studentTripsRepository;

    @Autowired
    private StudentTripsMapper studentTripsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentTripsMockMvc;

    private StudentTrips studentTrips;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentTrips createEntity(EntityManager em) {
        StudentTrips studentTrips = new StudentTrips()
            .status(DEFAULT_STATUS)
            .pickupTime(DEFAULT_PICKUP_TIME)
            .dropOffTime(DEFAULT_DROP_OFF_TIME)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return studentTrips;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentTrips createUpdatedEntity(EntityManager em) {
        StudentTrips studentTrips = new StudentTrips()
            .status(UPDATED_STATUS)
            .pickupTime(UPDATED_PICKUP_TIME)
            .dropOffTime(UPDATED_DROP_OFF_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return studentTrips;
    }

    @BeforeEach
    public void initTest() {
        studentTrips = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentTrips() throws Exception {
        int databaseSizeBeforeCreate = studentTripsRepository.findAll().size();
        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);
        restStudentTripsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentTrips testStudentTrips = studentTripsList.get(studentTripsList.size() - 1);
        assertThat(testStudentTrips.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStudentTrips.getPickupTime()).isEqualTo(DEFAULT_PICKUP_TIME);
        assertThat(testStudentTrips.getDropOffTime()).isEqualTo(DEFAULT_DROP_OFF_TIME);
        assertThat(testStudentTrips.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testStudentTrips.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testStudentTrips.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createStudentTripsWithExistingId() throws Exception {
        // Create the StudentTrips with an existing ID
        studentTrips.setId(1L);
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        int databaseSizeBeforeCreate = studentTripsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentTripsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentTripsRepository.findAll().size();
        // set the field null
        studentTrips.setStatus(null);

        // Create the StudentTrips, which fails.
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        restStudentTripsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentTrips() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList
        restStudentTripsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentTrips.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].pickupTime").value(hasItem(DEFAULT_PICKUP_TIME.toString())))
            .andExpect(jsonPath("$.[*].dropOffTime").value(hasItem(DEFAULT_DROP_OFF_TIME.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentTrips() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get the studentTrips
        restStudentTripsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentTrips.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentTrips.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.pickupTime").value(DEFAULT_PICKUP_TIME.toString()))
            .andExpect(jsonPath("$.dropOffTime").value(DEFAULT_DROP_OFF_TIME.toString()))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentTripsByIdFiltering() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        Long id = studentTrips.getId();

        defaultStudentTripsShouldBeFound("id.equals=" + id);
        defaultStudentTripsShouldNotBeFound("id.notEquals=" + id);

        defaultStudentTripsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentTripsShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentTripsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentTripsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentTripsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where status equals to DEFAULT_STATUS
        defaultStudentTripsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the studentTripsList where status equals to UPDATED_STATUS
        defaultStudentTripsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentTripsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultStudentTripsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the studentTripsList where status equals to UPDATED_STATUS
        defaultStudentTripsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentTripsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where status is not null
        defaultStudentTripsShouldBeFound("status.specified=true");

        // Get all the studentTripsList where status is null
        defaultStudentTripsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentTripsByPickupTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where pickupTime equals to DEFAULT_PICKUP_TIME
        defaultStudentTripsShouldBeFound("pickupTime.equals=" + DEFAULT_PICKUP_TIME);

        // Get all the studentTripsList where pickupTime equals to UPDATED_PICKUP_TIME
        defaultStudentTripsShouldNotBeFound("pickupTime.equals=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllStudentTripsByPickupTimeIsInShouldWork() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where pickupTime in DEFAULT_PICKUP_TIME or UPDATED_PICKUP_TIME
        defaultStudentTripsShouldBeFound("pickupTime.in=" + DEFAULT_PICKUP_TIME + "," + UPDATED_PICKUP_TIME);

        // Get all the studentTripsList where pickupTime equals to UPDATED_PICKUP_TIME
        defaultStudentTripsShouldNotBeFound("pickupTime.in=" + UPDATED_PICKUP_TIME);
    }

    @Test
    @Transactional
    void getAllStudentTripsByPickupTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where pickupTime is not null
        defaultStudentTripsShouldBeFound("pickupTime.specified=true");

        // Get all the studentTripsList where pickupTime is null
        defaultStudentTripsShouldNotBeFound("pickupTime.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentTripsByDropOffTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where dropOffTime equals to DEFAULT_DROP_OFF_TIME
        defaultStudentTripsShouldBeFound("dropOffTime.equals=" + DEFAULT_DROP_OFF_TIME);

        // Get all the studentTripsList where dropOffTime equals to UPDATED_DROP_OFF_TIME
        defaultStudentTripsShouldNotBeFound("dropOffTime.equals=" + UPDATED_DROP_OFF_TIME);
    }

    @Test
    @Transactional
    void getAllStudentTripsByDropOffTimeIsInShouldWork() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where dropOffTime in DEFAULT_DROP_OFF_TIME or UPDATED_DROP_OFF_TIME
        defaultStudentTripsShouldBeFound("dropOffTime.in=" + DEFAULT_DROP_OFF_TIME + "," + UPDATED_DROP_OFF_TIME);

        // Get all the studentTripsList where dropOffTime equals to UPDATED_DROP_OFF_TIME
        defaultStudentTripsShouldNotBeFound("dropOffTime.in=" + UPDATED_DROP_OFF_TIME);
    }

    @Test
    @Transactional
    void getAllStudentTripsByDropOffTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where dropOffTime is not null
        defaultStudentTripsShouldBeFound("dropOffTime.specified=true");

        // Get all the studentTripsList where dropOffTime is null
        defaultStudentTripsShouldNotBeFound("dropOffTime.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentTripsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultStudentTripsShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the studentTripsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultStudentTripsShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentTripsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultStudentTripsShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the studentTripsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultStudentTripsShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentTripsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where entityStatus is not null
        defaultStudentTripsShouldBeFound("entityStatus.specified=true");

        // Get all the studentTripsList where entityStatus is null
        defaultStudentTripsShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentTripsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where creationDate equals to DEFAULT_CREATION_DATE
        defaultStudentTripsShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the studentTripsList where creationDate equals to UPDATED_CREATION_DATE
        defaultStudentTripsShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllStudentTripsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultStudentTripsShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the studentTripsList where creationDate equals to UPDATED_CREATION_DATE
        defaultStudentTripsShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllStudentTripsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where creationDate is not null
        defaultStudentTripsShouldBeFound("creationDate.specified=true");

        // Get all the studentTripsList where creationDate is null
        defaultStudentTripsShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentTripsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultStudentTripsShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the studentTripsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultStudentTripsShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllStudentTripsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultStudentTripsShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the studentTripsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultStudentTripsShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllStudentTripsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        // Get all the studentTripsList where modifiedDate is not null
        defaultStudentTripsShouldBeFound("modifiedDate.specified=true");

        // Get all the studentTripsList where modifiedDate is null
        defaultStudentTripsShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentTripsByStudentIsEqualToSomething() throws Exception {
        Students student;
        if (TestUtil.findAll(em, Students.class).isEmpty()) {
            studentTripsRepository.saveAndFlush(studentTrips);
            student = StudentsResourceIT.createEntity(em);
        } else {
            student = TestUtil.findAll(em, Students.class).get(0);
        }
        em.persist(student);
        em.flush();
        studentTrips.setStudent(student);
        studentTripsRepository.saveAndFlush(studentTrips);
        Long studentId = student.getId();
        // Get all the studentTripsList where student equals to studentId
        defaultStudentTripsShouldBeFound("studentId.equals=" + studentId);

        // Get all the studentTripsList where student equals to (studentId + 1)
        defaultStudentTripsShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    @Test
    @Transactional
    void getAllStudentTripsByTripIsEqualToSomething() throws Exception {
        Trips trip;
        if (TestUtil.findAll(em, Trips.class).isEmpty()) {
            studentTripsRepository.saveAndFlush(studentTrips);
            trip = TripsResourceIT.createEntity(em);
        } else {
            trip = TestUtil.findAll(em, Trips.class).get(0);
        }
        em.persist(trip);
        em.flush();
        studentTrips.setTrip(trip);
        studentTripsRepository.saveAndFlush(studentTrips);
        Long tripId = trip.getId();
        // Get all the studentTripsList where trip equals to tripId
        defaultStudentTripsShouldBeFound("tripId.equals=" + tripId);

        // Get all the studentTripsList where trip equals to (tripId + 1)
        defaultStudentTripsShouldNotBeFound("tripId.equals=" + (tripId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentTripsShouldBeFound(String filter) throws Exception {
        restStudentTripsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentTrips.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].pickupTime").value(hasItem(DEFAULT_PICKUP_TIME.toString())))
            .andExpect(jsonPath("$.[*].dropOffTime").value(hasItem(DEFAULT_DROP_OFF_TIME.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentTripsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentTripsShouldNotBeFound(String filter) throws Exception {
        restStudentTripsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentTripsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentTrips() throws Exception {
        // Get the studentTrips
        restStudentTripsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentTrips() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();

        // Update the studentTrips
        StudentTrips updatedStudentTrips = studentTripsRepository.findById(studentTrips.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentTrips are not directly saved in db
        em.detach(updatedStudentTrips);
        updatedStudentTrips
            .status(UPDATED_STATUS)
            .pickupTime(UPDATED_PICKUP_TIME)
            .dropOffTime(UPDATED_DROP_OFF_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(updatedStudentTrips);

        restStudentTripsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentTripsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
        StudentTrips testStudentTrips = studentTripsList.get(studentTripsList.size() - 1);
        assertThat(testStudentTrips.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStudentTrips.getPickupTime()).isEqualTo(UPDATED_PICKUP_TIME);
        assertThat(testStudentTrips.getDropOffTime()).isEqualTo(UPDATED_DROP_OFF_TIME);
        assertThat(testStudentTrips.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testStudentTrips.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStudentTrips.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentTrips() throws Exception {
        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();
        studentTrips.setId(count.incrementAndGet());

        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentTripsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentTripsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentTrips() throws Exception {
        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();
        studentTrips.setId(count.incrementAndGet());

        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTripsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentTrips() throws Exception {
        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();
        studentTrips.setId(count.incrementAndGet());

        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTripsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentTripsWithPatch() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();

        // Update the studentTrips using partial update
        StudentTrips partialUpdatedStudentTrips = new StudentTrips();
        partialUpdatedStudentTrips.setId(studentTrips.getId());

        partialUpdatedStudentTrips
            .pickupTime(UPDATED_PICKUP_TIME)
            .dropOffTime(UPDATED_DROP_OFF_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restStudentTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentTrips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentTrips))
            )
            .andExpect(status().isOk());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
        StudentTrips testStudentTrips = studentTripsList.get(studentTripsList.size() - 1);
        assertThat(testStudentTrips.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStudentTrips.getPickupTime()).isEqualTo(UPDATED_PICKUP_TIME);
        assertThat(testStudentTrips.getDropOffTime()).isEqualTo(UPDATED_DROP_OFF_TIME);
        assertThat(testStudentTrips.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testStudentTrips.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStudentTrips.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentTripsWithPatch() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();

        // Update the studentTrips using partial update
        StudentTrips partialUpdatedStudentTrips = new StudentTrips();
        partialUpdatedStudentTrips.setId(studentTrips.getId());

        partialUpdatedStudentTrips
            .status(UPDATED_STATUS)
            .pickupTime(UPDATED_PICKUP_TIME)
            .dropOffTime(UPDATED_DROP_OFF_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restStudentTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentTrips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentTrips))
            )
            .andExpect(status().isOk());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
        StudentTrips testStudentTrips = studentTripsList.get(studentTripsList.size() - 1);
        assertThat(testStudentTrips.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStudentTrips.getPickupTime()).isEqualTo(UPDATED_PICKUP_TIME);
        assertThat(testStudentTrips.getDropOffTime()).isEqualTo(UPDATED_DROP_OFF_TIME);
        assertThat(testStudentTrips.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testStudentTrips.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStudentTrips.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentTrips() throws Exception {
        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();
        studentTrips.setId(count.incrementAndGet());

        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentTripsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentTrips() throws Exception {
        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();
        studentTrips.setId(count.incrementAndGet());

        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentTrips() throws Exception {
        int databaseSizeBeforeUpdate = studentTripsRepository.findAll().size();
        studentTrips.setId(count.incrementAndGet());

        // Create the StudentTrips
        StudentTripsDTO studentTripsDTO = studentTripsMapper.toDto(studentTrips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentTripsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentTripsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentTrips in the database
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentTrips() throws Exception {
        // Initialize the database
        studentTripsRepository.saveAndFlush(studentTrips);

        int databaseSizeBeforeDelete = studentTripsRepository.findAll().size();

        // Delete the studentTrips
        restStudentTripsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentTrips.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentTrips> studentTripsList = studentTripsRepository.findAll();
        assertThat(studentTripsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
