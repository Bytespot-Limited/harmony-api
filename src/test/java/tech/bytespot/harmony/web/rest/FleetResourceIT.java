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
import tech.bytespot.harmony.domain.Drivers;
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.domain.Schools;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.domain.Terminal;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.VehicleType;
import tech.bytespot.harmony.repository.FleetRepository;
import tech.bytespot.harmony.service.dto.FleetDTO;
import tech.bytespot.harmony.service.mapper.FleetMapper;

/**
 * Integration tests for the {@link FleetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FleetResourceIT {

    private static final String DEFAULT_NUMBER_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_PLATE = "BBBBBBBBBB";

    private static final VehicleType DEFAULT_VEHICLE_TYPE = VehicleType.BUS;
    private static final VehicleType UPDATED_VEHICLE_TYPE = VehicleType.VAN;

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fleets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FleetRepository fleetRepository;

    @Autowired
    private FleetMapper fleetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFleetMockMvc;

    private Fleet fleet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fleet createEntity(EntityManager em) {
        Fleet fleet = new Fleet()
            .numberPlate(DEFAULT_NUMBER_PLATE)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return fleet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fleet createUpdatedEntity(EntityManager em) {
        Fleet fleet = new Fleet()
            .numberPlate(UPDATED_NUMBER_PLATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return fleet;
    }

    @BeforeEach
    public void initTest() {
        fleet = createEntity(em);
    }

    @Test
    @Transactional
    void createFleet() throws Exception {
        int databaseSizeBeforeCreate = fleetRepository.findAll().size();
        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);
        restFleetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fleetDTO)))
            .andExpect(status().isCreated());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeCreate + 1);
        Fleet testFleet = fleetList.get(fleetList.size() - 1);
        assertThat(testFleet.getNumberPlate()).isEqualTo(DEFAULT_NUMBER_PLATE);
        assertThat(testFleet.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testFleet.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testFleet.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFleet.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createFleetWithExistingId() throws Exception {
        // Create the Fleet with an existing ID
        fleet.setId(1L);
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        int databaseSizeBeforeCreate = fleetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFleetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fleetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fleetRepository.findAll().size();
        // set the field null
        fleet.setNumberPlate(null);

        // Create the Fleet, which fails.
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        restFleetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fleetDTO)))
            .andExpect(status().isBadRequest());

        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fleetRepository.findAll().size();
        // set the field null
        fleet.setVehicleType(null);

        // Create the Fleet, which fails.
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        restFleetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fleetDTO)))
            .andExpect(status().isBadRequest());

        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFleets() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList
        restFleetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fleet.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberPlate").value(hasItem(DEFAULT_NUMBER_PLATE)))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFleet() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get the fleet
        restFleetMockMvc
            .perform(get(ENTITY_API_URL_ID, fleet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fleet.getId().intValue()))
            .andExpect(jsonPath("$.numberPlate").value(DEFAULT_NUMBER_PLATE))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFleetsByIdFiltering() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        Long id = fleet.getId();

        defaultFleetShouldBeFound("id.equals=" + id);
        defaultFleetShouldNotBeFound("id.notEquals=" + id);

        defaultFleetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFleetShouldNotBeFound("id.greaterThan=" + id);

        defaultFleetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFleetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFleetsByNumberPlateIsEqualToSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where numberPlate equals to DEFAULT_NUMBER_PLATE
        defaultFleetShouldBeFound("numberPlate.equals=" + DEFAULT_NUMBER_PLATE);

        // Get all the fleetList where numberPlate equals to UPDATED_NUMBER_PLATE
        defaultFleetShouldNotBeFound("numberPlate.equals=" + UPDATED_NUMBER_PLATE);
    }

    @Test
    @Transactional
    void getAllFleetsByNumberPlateIsInShouldWork() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where numberPlate in DEFAULT_NUMBER_PLATE or UPDATED_NUMBER_PLATE
        defaultFleetShouldBeFound("numberPlate.in=" + DEFAULT_NUMBER_PLATE + "," + UPDATED_NUMBER_PLATE);

        // Get all the fleetList where numberPlate equals to UPDATED_NUMBER_PLATE
        defaultFleetShouldNotBeFound("numberPlate.in=" + UPDATED_NUMBER_PLATE);
    }

    @Test
    @Transactional
    void getAllFleetsByNumberPlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where numberPlate is not null
        defaultFleetShouldBeFound("numberPlate.specified=true");

        // Get all the fleetList where numberPlate is null
        defaultFleetShouldNotBeFound("numberPlate.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetsByNumberPlateContainsSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where numberPlate contains DEFAULT_NUMBER_PLATE
        defaultFleetShouldBeFound("numberPlate.contains=" + DEFAULT_NUMBER_PLATE);

        // Get all the fleetList where numberPlate contains UPDATED_NUMBER_PLATE
        defaultFleetShouldNotBeFound("numberPlate.contains=" + UPDATED_NUMBER_PLATE);
    }

    @Test
    @Transactional
    void getAllFleetsByNumberPlateNotContainsSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where numberPlate does not contain DEFAULT_NUMBER_PLATE
        defaultFleetShouldNotBeFound("numberPlate.doesNotContain=" + DEFAULT_NUMBER_PLATE);

        // Get all the fleetList where numberPlate does not contain UPDATED_NUMBER_PLATE
        defaultFleetShouldBeFound("numberPlate.doesNotContain=" + UPDATED_NUMBER_PLATE);
    }

    @Test
    @Transactional
    void getAllFleetsByVehicleTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where vehicleType equals to DEFAULT_VEHICLE_TYPE
        defaultFleetShouldBeFound("vehicleType.equals=" + DEFAULT_VEHICLE_TYPE);

        // Get all the fleetList where vehicleType equals to UPDATED_VEHICLE_TYPE
        defaultFleetShouldNotBeFound("vehicleType.equals=" + UPDATED_VEHICLE_TYPE);
    }

    @Test
    @Transactional
    void getAllFleetsByVehicleTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where vehicleType in DEFAULT_VEHICLE_TYPE or UPDATED_VEHICLE_TYPE
        defaultFleetShouldBeFound("vehicleType.in=" + DEFAULT_VEHICLE_TYPE + "," + UPDATED_VEHICLE_TYPE);

        // Get all the fleetList where vehicleType equals to UPDATED_VEHICLE_TYPE
        defaultFleetShouldNotBeFound("vehicleType.in=" + UPDATED_VEHICLE_TYPE);
    }

    @Test
    @Transactional
    void getAllFleetsByVehicleTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where vehicleType is not null
        defaultFleetShouldBeFound("vehicleType.specified=true");

        // Get all the fleetList where vehicleType is null
        defaultFleetShouldNotBeFound("vehicleType.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultFleetShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the fleetList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultFleetShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllFleetsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultFleetShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the fleetList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultFleetShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllFleetsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where entityStatus is not null
        defaultFleetShouldBeFound("entityStatus.specified=true");

        // Get all the fleetList where entityStatus is null
        defaultFleetShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where creationDate equals to DEFAULT_CREATION_DATE
        defaultFleetShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the fleetList where creationDate equals to UPDATED_CREATION_DATE
        defaultFleetShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFleetsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultFleetShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the fleetList where creationDate equals to UPDATED_CREATION_DATE
        defaultFleetShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllFleetsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where creationDate is not null
        defaultFleetShouldBeFound("creationDate.specified=true");

        // Get all the fleetList where creationDate is null
        defaultFleetShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultFleetShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the fleetList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultFleetShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllFleetsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultFleetShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the fleetList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultFleetShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllFleetsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleetList where modifiedDate is not null
        defaultFleetShouldBeFound("modifiedDate.specified=true");

        // Get all the fleetList where modifiedDate is null
        defaultFleetShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetsByTerminalIsEqualToSomething() throws Exception {
        Terminal terminal;
        if (TestUtil.findAll(em, Terminal.class).isEmpty()) {
            fleetRepository.saveAndFlush(fleet);
            terminal = TerminalResourceIT.createEntity(em);
        } else {
            terminal = TestUtil.findAll(em, Terminal.class).get(0);
        }
        em.persist(terminal);
        em.flush();
        fleet.setTerminal(terminal);
        fleetRepository.saveAndFlush(fleet);
        Long terminalId = terminal.getId();
        // Get all the fleetList where terminal equals to terminalId
        defaultFleetShouldBeFound("terminalId.equals=" + terminalId);

        // Get all the fleetList where terminal equals to (terminalId + 1)
        defaultFleetShouldNotBeFound("terminalId.equals=" + (terminalId + 1));
    }

    @Test
    @Transactional
    void getAllFleetsByDriversIsEqualToSomething() throws Exception {
        Drivers drivers;
        if (TestUtil.findAll(em, Drivers.class).isEmpty()) {
            fleetRepository.saveAndFlush(fleet);
            drivers = DriversResourceIT.createEntity(em);
        } else {
            drivers = TestUtil.findAll(em, Drivers.class).get(0);
        }
        em.persist(drivers);
        em.flush();
        fleet.addDrivers(drivers);
        fleetRepository.saveAndFlush(fleet);
        Long driversId = drivers.getId();
        // Get all the fleetList where drivers equals to driversId
        defaultFleetShouldBeFound("driversId.equals=" + driversId);

        // Get all the fleetList where drivers equals to (driversId + 1)
        defaultFleetShouldNotBeFound("driversId.equals=" + (driversId + 1));
    }

    @Test
    @Transactional
    void getAllFleetsByStudentsIsEqualToSomething() throws Exception {
        Students students;
        if (TestUtil.findAll(em, Students.class).isEmpty()) {
            fleetRepository.saveAndFlush(fleet);
            students = StudentsResourceIT.createEntity(em);
        } else {
            students = TestUtil.findAll(em, Students.class).get(0);
        }
        em.persist(students);
        em.flush();
        fleet.addStudents(students);
        fleetRepository.saveAndFlush(fleet);
        Long studentsId = students.getId();
        // Get all the fleetList where students equals to studentsId
        defaultFleetShouldBeFound("studentsId.equals=" + studentsId);

        // Get all the fleetList where students equals to (studentsId + 1)
        defaultFleetShouldNotBeFound("studentsId.equals=" + (studentsId + 1));
    }

    @Test
    @Transactional
    void getAllFleetsBySchoolIsEqualToSomething() throws Exception {
        Schools school;
        if (TestUtil.findAll(em, Schools.class).isEmpty()) {
            fleetRepository.saveAndFlush(fleet);
            school = SchoolsResourceIT.createEntity(em);
        } else {
            school = TestUtil.findAll(em, Schools.class).get(0);
        }
        em.persist(school);
        em.flush();
        fleet.setSchool(school);
        fleetRepository.saveAndFlush(fleet);
        Long schoolId = school.getId();
        // Get all the fleetList where school equals to schoolId
        defaultFleetShouldBeFound("schoolId.equals=" + schoolId);

        // Get all the fleetList where school equals to (schoolId + 1)
        defaultFleetShouldNotBeFound("schoolId.equals=" + (schoolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFleetShouldBeFound(String filter) throws Exception {
        restFleetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fleet.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberPlate").value(hasItem(DEFAULT_NUMBER_PLATE)))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restFleetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFleetShouldNotBeFound(String filter) throws Exception {
        restFleetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFleetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFleet() throws Exception {
        // Get the fleet
        restFleetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFleet() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();

        // Update the fleet
        Fleet updatedFleet = fleetRepository.findById(fleet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFleet are not directly saved in db
        em.detach(updatedFleet);
        updatedFleet
            .numberPlate(UPDATED_NUMBER_PLATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        FleetDTO fleetDTO = fleetMapper.toDto(updatedFleet);

        restFleetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fleetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fleetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
        Fleet testFleet = fleetList.get(fleetList.size() - 1);
        assertThat(testFleet.getNumberPlate()).isEqualTo(UPDATED_NUMBER_PLATE);
        assertThat(testFleet.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testFleet.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testFleet.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFleet.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFleet() throws Exception {
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();
        fleet.setId(count.incrementAndGet());

        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFleetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fleetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fleetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFleet() throws Exception {
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();
        fleet.setId(count.incrementAndGet());

        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fleetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFleet() throws Exception {
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();
        fleet.setId(count.incrementAndGet());

        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fleetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFleetWithPatch() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();

        // Update the fleet using partial update
        Fleet partialUpdatedFleet = new Fleet();
        partialUpdatedFleet.setId(fleet.getId());

        partialUpdatedFleet
            .numberPlate(UPDATED_NUMBER_PLATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restFleetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFleet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFleet))
            )
            .andExpect(status().isOk());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
        Fleet testFleet = fleetList.get(fleetList.size() - 1);
        assertThat(testFleet.getNumberPlate()).isEqualTo(UPDATED_NUMBER_PLATE);
        assertThat(testFleet.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testFleet.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testFleet.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFleet.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFleetWithPatch() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();

        // Update the fleet using partial update
        Fleet partialUpdatedFleet = new Fleet();
        partialUpdatedFleet.setId(fleet.getId());

        partialUpdatedFleet
            .numberPlate(UPDATED_NUMBER_PLATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restFleetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFleet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFleet))
            )
            .andExpect(status().isOk());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
        Fleet testFleet = fleetList.get(fleetList.size() - 1);
        assertThat(testFleet.getNumberPlate()).isEqualTo(UPDATED_NUMBER_PLATE);
        assertThat(testFleet.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testFleet.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testFleet.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFleet.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFleet() throws Exception {
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();
        fleet.setId(count.incrementAndGet());

        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFleetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fleetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fleetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFleet() throws Exception {
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();
        fleet.setId(count.incrementAndGet());

        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fleetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFleet() throws Exception {
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();
        fleet.setId(count.incrementAndGet());

        // Create the Fleet
        FleetDTO fleetDTO = fleetMapper.toDto(fleet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fleetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fleet in the database
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFleet() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        int databaseSizeBeforeDelete = fleetRepository.findAll().size();

        // Delete the fleet
        restFleetMockMvc
            .perform(delete(ENTITY_API_URL_ID, fleet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fleet> fleetList = fleetRepository.findAll();
        assertThat(fleetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
