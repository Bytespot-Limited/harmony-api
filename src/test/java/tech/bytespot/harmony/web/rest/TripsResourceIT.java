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
import tech.bytespot.harmony.domain.Trips;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.TripStatus;
import tech.bytespot.harmony.domain.enumeration.TripType;
import tech.bytespot.harmony.repository.TripsRepository;
import tech.bytespot.harmony.service.dto.TripsDTO;
import tech.bytespot.harmony.service.mapper.TripsMapper;

/**
 * Integration tests for the {@link TripsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripsResourceIT {

    private static final TripType DEFAULT_TRIP_TYPE = TripType.PICKUP;
    private static final TripType UPDATED_TRIP_TYPE = TripType.DROPOFF;

    private static final TripStatus DEFAULT_TRIP_STATUS = TripStatus.STARTED;
    private static final TripStatus UPDATED_TRIP_STATUS = TripStatus.ONGOING;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripsRepository tripsRepository;

    @Autowired
    private TripsMapper tripsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripsMockMvc;

    private Trips trips;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trips createEntity(EntityManager em) {
        Trips trips = new Trips()
            .tripType(DEFAULT_TRIP_TYPE)
            .tripStatus(DEFAULT_TRIP_STATUS)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return trips;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trips createUpdatedEntity(EntityManager em) {
        Trips trips = new Trips()
            .tripType(UPDATED_TRIP_TYPE)
            .tripStatus(UPDATED_TRIP_STATUS)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return trips;
    }

    @BeforeEach
    public void initTest() {
        trips = createEntity(em);
    }

    @Test
    @Transactional
    void createTrips() throws Exception {
        int databaseSizeBeforeCreate = tripsRepository.findAll().size();
        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);
        restTripsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isCreated());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeCreate + 1);
        Trips testTrips = tripsList.get(tripsList.size() - 1);
        assertThat(testTrips.getTripType()).isEqualTo(DEFAULT_TRIP_TYPE);
        assertThat(testTrips.getTripStatus()).isEqualTo(DEFAULT_TRIP_STATUS);
        assertThat(testTrips.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTrips.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTrips.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testTrips.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTrips.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTripsWithExistingId() throws Exception {
        // Create the Trips with an existing ID
        trips.setId(1L);
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        int databaseSizeBeforeCreate = tripsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTripTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripsRepository.findAll().size();
        // set the field null
        trips.setTripType(null);

        // Create the Trips, which fails.
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        restTripsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isBadRequest());

        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTripStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripsRepository.findAll().size();
        // set the field null
        trips.setTripStatus(null);

        // Create the Trips, which fails.
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        restTripsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isBadRequest());

        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripsRepository.findAll().size();
        // set the field null
        trips.setStartTime(null);

        // Create the Trips, which fails.
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        restTripsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isBadRequest());

        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList
        restTripsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trips.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripType").value(hasItem(DEFAULT_TRIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tripStatus").value(hasItem(DEFAULT_TRIP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTrips() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get the trips
        restTripsMockMvc
            .perform(get(ENTITY_API_URL_ID, trips.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trips.getId().intValue()))
            .andExpect(jsonPath("$.tripType").value(DEFAULT_TRIP_TYPE.toString()))
            .andExpect(jsonPath("$.tripStatus").value(DEFAULT_TRIP_STATUS.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTripsByIdFiltering() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        Long id = trips.getId();

        defaultTripsShouldBeFound("id.equals=" + id);
        defaultTripsShouldNotBeFound("id.notEquals=" + id);

        defaultTripsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTripsShouldNotBeFound("id.greaterThan=" + id);

        defaultTripsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTripsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripsByTripTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where tripType equals to DEFAULT_TRIP_TYPE
        defaultTripsShouldBeFound("tripType.equals=" + DEFAULT_TRIP_TYPE);

        // Get all the tripsList where tripType equals to UPDATED_TRIP_TYPE
        defaultTripsShouldNotBeFound("tripType.equals=" + UPDATED_TRIP_TYPE);
    }

    @Test
    @Transactional
    void getAllTripsByTripTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where tripType in DEFAULT_TRIP_TYPE or UPDATED_TRIP_TYPE
        defaultTripsShouldBeFound("tripType.in=" + DEFAULT_TRIP_TYPE + "," + UPDATED_TRIP_TYPE);

        // Get all the tripsList where tripType equals to UPDATED_TRIP_TYPE
        defaultTripsShouldNotBeFound("tripType.in=" + UPDATED_TRIP_TYPE);
    }

    @Test
    @Transactional
    void getAllTripsByTripTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where tripType is not null
        defaultTripsShouldBeFound("tripType.specified=true");

        // Get all the tripsList where tripType is null
        defaultTripsShouldNotBeFound("tripType.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByTripStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where tripStatus equals to DEFAULT_TRIP_STATUS
        defaultTripsShouldBeFound("tripStatus.equals=" + DEFAULT_TRIP_STATUS);

        // Get all the tripsList where tripStatus equals to UPDATED_TRIP_STATUS
        defaultTripsShouldNotBeFound("tripStatus.equals=" + UPDATED_TRIP_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByTripStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where tripStatus in DEFAULT_TRIP_STATUS or UPDATED_TRIP_STATUS
        defaultTripsShouldBeFound("tripStatus.in=" + DEFAULT_TRIP_STATUS + "," + UPDATED_TRIP_STATUS);

        // Get all the tripsList where tripStatus equals to UPDATED_TRIP_STATUS
        defaultTripsShouldNotBeFound("tripStatus.in=" + UPDATED_TRIP_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByTripStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where tripStatus is not null
        defaultTripsShouldBeFound("tripStatus.specified=true");

        // Get all the tripsList where tripStatus is null
        defaultTripsShouldNotBeFound("tripStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where startTime equals to DEFAULT_START_TIME
        defaultTripsShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the tripsList where startTime equals to UPDATED_START_TIME
        defaultTripsShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultTripsShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the tripsList where startTime equals to UPDATED_START_TIME
        defaultTripsShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where startTime is not null
        defaultTripsShouldBeFound("startTime.specified=true");

        // Get all the tripsList where startTime is null
        defaultTripsShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where endTime equals to DEFAULT_END_TIME
        defaultTripsShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the tripsList where endTime equals to UPDATED_END_TIME
        defaultTripsShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultTripsShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the tripsList where endTime equals to UPDATED_END_TIME
        defaultTripsShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where endTime is not null
        defaultTripsShouldBeFound("endTime.specified=true");

        // Get all the tripsList where endTime is null
        defaultTripsShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultTripsShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the tripsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultTripsShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultTripsShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the tripsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultTripsShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where entityStatus is not null
        defaultTripsShouldBeFound("entityStatus.specified=true");

        // Get all the tripsList where entityStatus is null
        defaultTripsShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where creationDate equals to DEFAULT_CREATION_DATE
        defaultTripsShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the tripsList where creationDate equals to UPDATED_CREATION_DATE
        defaultTripsShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultTripsShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the tripsList where creationDate equals to UPDATED_CREATION_DATE
        defaultTripsShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where creationDate is not null
        defaultTripsShouldBeFound("creationDate.specified=true");

        // Get all the tripsList where creationDate is null
        defaultTripsShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultTripsShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the tripsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultTripsShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultTripsShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the tripsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultTripsShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTripsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        // Get all the tripsList where modifiedDate is not null
        defaultTripsShouldBeFound("modifiedDate.specified=true");

        // Get all the tripsList where modifiedDate is null
        defaultTripsShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByStudentTripsIsEqualToSomething() throws Exception {
        StudentTrips studentTrips;
        if (TestUtil.findAll(em, StudentTrips.class).isEmpty()) {
            tripsRepository.saveAndFlush(trips);
            studentTrips = StudentTripsResourceIT.createEntity(em);
        } else {
            studentTrips = TestUtil.findAll(em, StudentTrips.class).get(0);
        }
        em.persist(studentTrips);
        em.flush();
        trips.addStudentTrips(studentTrips);
        tripsRepository.saveAndFlush(trips);
        Long studentTripsId = studentTrips.getId();
        // Get all the tripsList where studentTrips equals to studentTripsId
        defaultTripsShouldBeFound("studentTripsId.equals=" + studentTripsId);

        // Get all the tripsList where studentTrips equals to (studentTripsId + 1)
        defaultTripsShouldNotBeFound("studentTripsId.equals=" + (studentTripsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripsShouldBeFound(String filter) throws Exception {
        restTripsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trips.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripType").value(hasItem(DEFAULT_TRIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tripStatus").value(hasItem(DEFAULT_TRIP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restTripsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripsShouldNotBeFound(String filter) throws Exception {
        restTripsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrips() throws Exception {
        // Get the trips
        restTripsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrips() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();

        // Update the trips
        Trips updatedTrips = tripsRepository.findById(trips.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrips are not directly saved in db
        em.detach(updatedTrips);
        updatedTrips
            .tripType(UPDATED_TRIP_TYPE)
            .tripStatus(UPDATED_TRIP_STATUS)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        TripsDTO tripsDTO = tripsMapper.toDto(updatedTrips);

        restTripsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
        Trips testTrips = tripsList.get(tripsList.size() - 1);
        assertThat(testTrips.getTripType()).isEqualTo(UPDATED_TRIP_TYPE);
        assertThat(testTrips.getTripStatus()).isEqualTo(UPDATED_TRIP_STATUS);
        assertThat(testTrips.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrips.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTrips.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testTrips.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTrips.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTrips() throws Exception {
        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();
        trips.setId(count.incrementAndGet());

        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrips() throws Exception {
        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();
        trips.setId(count.incrementAndGet());

        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrips() throws Exception {
        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();
        trips.setId(count.incrementAndGet());

        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripsWithPatch() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();

        // Update the trips using partial update
        Trips partialUpdatedTrips = new Trips();
        partialUpdatedTrips.setId(trips.getId());

        partialUpdatedTrips.tripType(UPDATED_TRIP_TYPE).startTime(UPDATED_START_TIME).creationDate(UPDATED_CREATION_DATE);

        restTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrips))
            )
            .andExpect(status().isOk());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
        Trips testTrips = tripsList.get(tripsList.size() - 1);
        assertThat(testTrips.getTripType()).isEqualTo(UPDATED_TRIP_TYPE);
        assertThat(testTrips.getTripStatus()).isEqualTo(DEFAULT_TRIP_STATUS);
        assertThat(testTrips.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrips.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTrips.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testTrips.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTrips.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTripsWithPatch() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();

        // Update the trips using partial update
        Trips partialUpdatedTrips = new Trips();
        partialUpdatedTrips.setId(trips.getId());

        partialUpdatedTrips
            .tripType(UPDATED_TRIP_TYPE)
            .tripStatus(UPDATED_TRIP_STATUS)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrips.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrips))
            )
            .andExpect(status().isOk());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
        Trips testTrips = tripsList.get(tripsList.size() - 1);
        assertThat(testTrips.getTripType()).isEqualTo(UPDATED_TRIP_TYPE);
        assertThat(testTrips.getTripStatus()).isEqualTo(UPDATED_TRIP_STATUS);
        assertThat(testTrips.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrips.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTrips.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testTrips.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTrips.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTrips() throws Exception {
        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();
        trips.setId(count.incrementAndGet());

        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrips() throws Exception {
        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();
        trips.setId(count.incrementAndGet());

        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrips() throws Exception {
        int databaseSizeBeforeUpdate = tripsRepository.findAll().size();
        trips.setId(count.incrementAndGet());

        // Create the Trips
        TripsDTO tripsDTO = tripsMapper.toDto(trips);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trips in the database
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrips() throws Exception {
        // Initialize the database
        tripsRepository.saveAndFlush(trips);

        int databaseSizeBeforeDelete = tripsRepository.findAll().size();

        // Delete the trips
        restTripsMockMvc
            .perform(delete(ENTITY_API_URL_ID, trips.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trips> tripsList = tripsRepository.findAll();
        assertThat(tripsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
