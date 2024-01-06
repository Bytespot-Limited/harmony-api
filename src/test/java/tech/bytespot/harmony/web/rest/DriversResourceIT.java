package tech.bytespot.harmony.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import tech.bytespot.harmony.domain.enumeration.DriverAssignmentStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.repository.DriversRepository;
import tech.bytespot.harmony.service.dto.DriversDTO;
import tech.bytespot.harmony.service.mapper.DriversMapper;

/**
 * Integration tests for the {@link DriversResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DriversResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;
    private static final Integer SMALLER_USER_ID = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NATIONAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_NATIONAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final DriverAssignmentStatus DEFAULT_ASSIGNMENT_STATUS = DriverAssignmentStatus.ASSIGNED;
    private static final DriverAssignmentStatus UPDATED_ASSIGNMENT_STATUS = DriverAssignmentStatus.UNASSIGNED;

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DriversRepository driversRepository;

    @Autowired
    private DriversMapper driversMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriversMockMvc;

    private Drivers drivers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drivers createEntity(EntityManager em) {
        Drivers drivers = new Drivers()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .dob(DEFAULT_DOB)
            .nationalId(DEFAULT_NATIONAL_ID)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .assignmentStatus(DEFAULT_ASSIGNMENT_STATUS)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return drivers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drivers createUpdatedEntity(EntityManager em) {
        Drivers drivers = new Drivers()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .assignmentStatus(UPDATED_ASSIGNMENT_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return drivers;
    }

    @BeforeEach
    public void initTest() {
        drivers = createEntity(em);
    }

    @Test
    @Transactional
    void createDrivers() throws Exception {
        int databaseSizeBeforeCreate = driversRepository.findAll().size();
        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);
        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isCreated());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeCreate + 1);
        Drivers testDrivers = driversList.get(driversList.size() - 1);
        assertThat(testDrivers.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testDrivers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDrivers.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testDrivers.getNationalId()).isEqualTo(DEFAULT_NATIONAL_ID);
        assertThat(testDrivers.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testDrivers.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testDrivers.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testDrivers.getAssignmentStatus()).isEqualTo(DEFAULT_ASSIGNMENT_STATUS);
        assertThat(testDrivers.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testDrivers.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDrivers.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createDriversWithExistingId() throws Exception {
        // Create the Drivers with an existing ID
        drivers.setId(1L);
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        int databaseSizeBeforeCreate = driversRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = driversRepository.findAll().size();
        // set the field null
        drivers.setName(null);

        // Create the Drivers, which fails.
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = driversRepository.findAll().size();
        // set the field null
        drivers.setDob(null);

        // Create the Drivers, which fails.
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNationalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = driversRepository.findAll().size();
        // set the field null
        drivers.setNationalId(null);

        // Create the Drivers, which fails.
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = driversRepository.findAll().size();
        // set the field null
        drivers.setEmailAddress(null);

        // Create the Drivers, which fails.
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = driversRepository.findAll().size();
        // set the field null
        drivers.setPhoneNumber(null);

        // Create the Drivers, which fails.
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssignmentStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = driversRepository.findAll().size();
        // set the field null
        drivers.setAssignmentStatus(null);

        // Create the Drivers, which fails.
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        restDriversMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isBadRequest());

        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDrivers() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList
        restDriversMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drivers.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].assignmentStatus").value(hasItem(DEFAULT_ASSIGNMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDrivers() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get the drivers
        restDriversMockMvc
            .perform(get(ENTITY_API_URL_ID, drivers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(drivers.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.nationalId").value(DEFAULT_NATIONAL_ID))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.assignmentStatus").value(DEFAULT_ASSIGNMENT_STATUS.toString()))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDriversByIdFiltering() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        Long id = drivers.getId();

        defaultDriversShouldBeFound("id.equals=" + id);
        defaultDriversShouldNotBeFound("id.notEquals=" + id);

        defaultDriversShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDriversShouldNotBeFound("id.greaterThan=" + id);

        defaultDriversShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDriversShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId equals to DEFAULT_USER_ID
        defaultDriversShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the driversList where userId equals to UPDATED_USER_ID
        defaultDriversShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultDriversShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the driversList where userId equals to UPDATED_USER_ID
        defaultDriversShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId is not null
        defaultDriversShouldBeFound("userId.specified=true");

        // Get all the driversList where userId is null
        defaultDriversShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId is greater than or equal to DEFAULT_USER_ID
        defaultDriversShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the driversList where userId is greater than or equal to UPDATED_USER_ID
        defaultDriversShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId is less than or equal to DEFAULT_USER_ID
        defaultDriversShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the driversList where userId is less than or equal to SMALLER_USER_ID
        defaultDriversShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId is less than DEFAULT_USER_ID
        defaultDriversShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the driversList where userId is less than UPDATED_USER_ID
        defaultDriversShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where userId is greater than DEFAULT_USER_ID
        defaultDriversShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the driversList where userId is greater than SMALLER_USER_ID
        defaultDriversShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where name equals to DEFAULT_NAME
        defaultDriversShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the driversList where name equals to UPDATED_NAME
        defaultDriversShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByNameIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDriversShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the driversList where name equals to UPDATED_NAME
        defaultDriversShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where name is not null
        defaultDriversShouldBeFound("name.specified=true");

        // Get all the driversList where name is null
        defaultDriversShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByNameContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where name contains DEFAULT_NAME
        defaultDriversShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the driversList where name contains UPDATED_NAME
        defaultDriversShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByNameNotContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where name does not contain DEFAULT_NAME
        defaultDriversShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the driversList where name does not contain UPDATED_NAME
        defaultDriversShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob equals to DEFAULT_DOB
        defaultDriversShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the driversList where dob equals to UPDATED_DOB
        defaultDriversShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllDriversByDobIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultDriversShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the driversList where dob equals to UPDATED_DOB
        defaultDriversShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllDriversByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob is not null
        defaultDriversShouldBeFound("dob.specified=true");

        // Get all the driversList where dob is null
        defaultDriversShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob is greater than or equal to DEFAULT_DOB
        defaultDriversShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the driversList where dob is greater than or equal to UPDATED_DOB
        defaultDriversShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllDriversByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob is less than or equal to DEFAULT_DOB
        defaultDriversShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the driversList where dob is less than or equal to SMALLER_DOB
        defaultDriversShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllDriversByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob is less than DEFAULT_DOB
        defaultDriversShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the driversList where dob is less than UPDATED_DOB
        defaultDriversShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllDriversByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where dob is greater than DEFAULT_DOB
        defaultDriversShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the driversList where dob is greater than SMALLER_DOB
        defaultDriversShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllDriversByNationalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where nationalId equals to DEFAULT_NATIONAL_ID
        defaultDriversShouldBeFound("nationalId.equals=" + DEFAULT_NATIONAL_ID);

        // Get all the driversList where nationalId equals to UPDATED_NATIONAL_ID
        defaultDriversShouldNotBeFound("nationalId.equals=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllDriversByNationalIdIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where nationalId in DEFAULT_NATIONAL_ID or UPDATED_NATIONAL_ID
        defaultDriversShouldBeFound("nationalId.in=" + DEFAULT_NATIONAL_ID + "," + UPDATED_NATIONAL_ID);

        // Get all the driversList where nationalId equals to UPDATED_NATIONAL_ID
        defaultDriversShouldNotBeFound("nationalId.in=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllDriversByNationalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where nationalId is not null
        defaultDriversShouldBeFound("nationalId.specified=true");

        // Get all the driversList where nationalId is null
        defaultDriversShouldNotBeFound("nationalId.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByNationalIdContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where nationalId contains DEFAULT_NATIONAL_ID
        defaultDriversShouldBeFound("nationalId.contains=" + DEFAULT_NATIONAL_ID);

        // Get all the driversList where nationalId contains UPDATED_NATIONAL_ID
        defaultDriversShouldNotBeFound("nationalId.contains=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllDriversByNationalIdNotContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where nationalId does not contain DEFAULT_NATIONAL_ID
        defaultDriversShouldNotBeFound("nationalId.doesNotContain=" + DEFAULT_NATIONAL_ID);

        // Get all the driversList where nationalId does not contain UPDATED_NATIONAL_ID
        defaultDriversShouldBeFound("nationalId.doesNotContain=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllDriversByProfileImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where profileImageUrl equals to DEFAULT_PROFILE_IMAGE_URL
        defaultDriversShouldBeFound("profileImageUrl.equals=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the driversList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultDriversShouldNotBeFound("profileImageUrl.equals=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllDriversByProfileImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where profileImageUrl in DEFAULT_PROFILE_IMAGE_URL or UPDATED_PROFILE_IMAGE_URL
        defaultDriversShouldBeFound("profileImageUrl.in=" + DEFAULT_PROFILE_IMAGE_URL + "," + UPDATED_PROFILE_IMAGE_URL);

        // Get all the driversList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultDriversShouldNotBeFound("profileImageUrl.in=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllDriversByProfileImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where profileImageUrl is not null
        defaultDriversShouldBeFound("profileImageUrl.specified=true");

        // Get all the driversList where profileImageUrl is null
        defaultDriversShouldNotBeFound("profileImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByProfileImageUrlContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where profileImageUrl contains DEFAULT_PROFILE_IMAGE_URL
        defaultDriversShouldBeFound("profileImageUrl.contains=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the driversList where profileImageUrl contains UPDATED_PROFILE_IMAGE_URL
        defaultDriversShouldNotBeFound("profileImageUrl.contains=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllDriversByProfileImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where profileImageUrl does not contain DEFAULT_PROFILE_IMAGE_URL
        defaultDriversShouldNotBeFound("profileImageUrl.doesNotContain=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the driversList where profileImageUrl does not contain UPDATED_PROFILE_IMAGE_URL
        defaultDriversShouldBeFound("profileImageUrl.doesNotContain=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllDriversByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultDriversShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the driversList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultDriversShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDriversByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultDriversShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the driversList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultDriversShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDriversByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where emailAddress is not null
        defaultDriversShouldBeFound("emailAddress.specified=true");

        // Get all the driversList where emailAddress is null
        defaultDriversShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultDriversShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the driversList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultDriversShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDriversByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultDriversShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the driversList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultDriversShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDriversByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultDriversShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the driversList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultDriversShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultDriversShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the driversList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultDriversShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where phoneNumber is not null
        defaultDriversShouldBeFound("phoneNumber.specified=true");

        // Get all the driversList where phoneNumber is null
        defaultDriversShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultDriversShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the driversList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultDriversShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultDriversShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the driversList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultDriversShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByAssignmentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where assignmentStatus equals to DEFAULT_ASSIGNMENT_STATUS
        defaultDriversShouldBeFound("assignmentStatus.equals=" + DEFAULT_ASSIGNMENT_STATUS);

        // Get all the driversList where assignmentStatus equals to UPDATED_ASSIGNMENT_STATUS
        defaultDriversShouldNotBeFound("assignmentStatus.equals=" + UPDATED_ASSIGNMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllDriversByAssignmentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where assignmentStatus in DEFAULT_ASSIGNMENT_STATUS or UPDATED_ASSIGNMENT_STATUS
        defaultDriversShouldBeFound("assignmentStatus.in=" + DEFAULT_ASSIGNMENT_STATUS + "," + UPDATED_ASSIGNMENT_STATUS);

        // Get all the driversList where assignmentStatus equals to UPDATED_ASSIGNMENT_STATUS
        defaultDriversShouldNotBeFound("assignmentStatus.in=" + UPDATED_ASSIGNMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllDriversByAssignmentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where assignmentStatus is not null
        defaultDriversShouldBeFound("assignmentStatus.specified=true");

        // Get all the driversList where assignmentStatus is null
        defaultDriversShouldNotBeFound("assignmentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultDriversShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the driversList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultDriversShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllDriversByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultDriversShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the driversList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultDriversShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllDriversByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where entityStatus is not null
        defaultDriversShouldBeFound("entityStatus.specified=true");

        // Get all the driversList where entityStatus is null
        defaultDriversShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where creationDate equals to DEFAULT_CREATION_DATE
        defaultDriversShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the driversList where creationDate equals to UPDATED_CREATION_DATE
        defaultDriversShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllDriversByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultDriversShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the driversList where creationDate equals to UPDATED_CREATION_DATE
        defaultDriversShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllDriversByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where creationDate is not null
        defaultDriversShouldBeFound("creationDate.specified=true");

        // Get all the driversList where creationDate is null
        defaultDriversShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultDriversShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the driversList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultDriversShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDriversByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultDriversShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the driversList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultDriversShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDriversByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        // Get all the driversList where modifiedDate is not null
        defaultDriversShouldBeFound("modifiedDate.specified=true");

        // Get all the driversList where modifiedDate is null
        defaultDriversShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByFleetIsEqualToSomething() throws Exception {
        Fleet fleet;
        if (TestUtil.findAll(em, Fleet.class).isEmpty()) {
            driversRepository.saveAndFlush(drivers);
            fleet = FleetResourceIT.createEntity(em);
        } else {
            fleet = TestUtil.findAll(em, Fleet.class).get(0);
        }
        em.persist(fleet);
        em.flush();
        drivers.setFleet(fleet);
        driversRepository.saveAndFlush(drivers);
        Long fleetId = fleet.getId();
        // Get all the driversList where fleet equals to fleetId
        defaultDriversShouldBeFound("fleetId.equals=" + fleetId);

        // Get all the driversList where fleet equals to (fleetId + 1)
        defaultDriversShouldNotBeFound("fleetId.equals=" + (fleetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDriversShouldBeFound(String filter) throws Exception {
        restDriversMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drivers.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].assignmentStatus").value(hasItem(DEFAULT_ASSIGNMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restDriversMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDriversShouldNotBeFound(String filter) throws Exception {
        restDriversMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDriversMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDrivers() throws Exception {
        // Get the drivers
        restDriversMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDrivers() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        int databaseSizeBeforeUpdate = driversRepository.findAll().size();

        // Update the drivers
        Drivers updatedDrivers = driversRepository.findById(drivers.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDrivers are not directly saved in db
        em.detach(updatedDrivers);
        updatedDrivers
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .assignmentStatus(UPDATED_ASSIGNMENT_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        DriversDTO driversDTO = driversMapper.toDto(updatedDrivers);

        restDriversMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driversDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driversDTO))
            )
            .andExpect(status().isOk());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
        Drivers testDrivers = driversList.get(driversList.size() - 1);
        assertThat(testDrivers.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDrivers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDrivers.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDrivers.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testDrivers.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testDrivers.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testDrivers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testDrivers.getAssignmentStatus()).isEqualTo(UPDATED_ASSIGNMENT_STATUS);
        assertThat(testDrivers.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testDrivers.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDrivers.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDrivers() throws Exception {
        int databaseSizeBeforeUpdate = driversRepository.findAll().size();
        drivers.setId(count.incrementAndGet());

        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriversMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driversDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driversDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDrivers() throws Exception {
        int databaseSizeBeforeUpdate = driversRepository.findAll().size();
        drivers.setId(count.incrementAndGet());

        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriversMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driversDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDrivers() throws Exception {
        int databaseSizeBeforeUpdate = driversRepository.findAll().size();
        drivers.setId(count.incrementAndGet());

        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriversMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driversDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriversWithPatch() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        int databaseSizeBeforeUpdate = driversRepository.findAll().size();

        // Update the drivers using partial update
        Drivers partialUpdatedDrivers = new Drivers();
        partialUpdatedDrivers.setId(drivers.getId());

        partialUpdatedDrivers
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restDriversMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrivers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDrivers))
            )
            .andExpect(status().isOk());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
        Drivers testDrivers = driversList.get(driversList.size() - 1);
        assertThat(testDrivers.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDrivers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDrivers.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDrivers.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testDrivers.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testDrivers.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testDrivers.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testDrivers.getAssignmentStatus()).isEqualTo(DEFAULT_ASSIGNMENT_STATUS);
        assertThat(testDrivers.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testDrivers.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDrivers.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDriversWithPatch() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        int databaseSizeBeforeUpdate = driversRepository.findAll().size();

        // Update the drivers using partial update
        Drivers partialUpdatedDrivers = new Drivers();
        partialUpdatedDrivers.setId(drivers.getId());

        partialUpdatedDrivers
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .assignmentStatus(UPDATED_ASSIGNMENT_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restDriversMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrivers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDrivers))
            )
            .andExpect(status().isOk());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
        Drivers testDrivers = driversList.get(driversList.size() - 1);
        assertThat(testDrivers.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDrivers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDrivers.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDrivers.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testDrivers.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testDrivers.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testDrivers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testDrivers.getAssignmentStatus()).isEqualTo(UPDATED_ASSIGNMENT_STATUS);
        assertThat(testDrivers.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testDrivers.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDrivers.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDrivers() throws Exception {
        int databaseSizeBeforeUpdate = driversRepository.findAll().size();
        drivers.setId(count.incrementAndGet());

        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriversMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driversDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driversDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDrivers() throws Exception {
        int databaseSizeBeforeUpdate = driversRepository.findAll().size();
        drivers.setId(count.incrementAndGet());

        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriversMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driversDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDrivers() throws Exception {
        int databaseSizeBeforeUpdate = driversRepository.findAll().size();
        drivers.setId(count.incrementAndGet());

        // Create the Drivers
        DriversDTO driversDTO = driversMapper.toDto(drivers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriversMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(driversDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drivers in the database
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDrivers() throws Exception {
        // Initialize the database
        driversRepository.saveAndFlush(drivers);

        int databaseSizeBeforeDelete = driversRepository.findAll().size();

        // Delete the drivers
        restDriversMockMvc
            .perform(delete(ENTITY_API_URL_ID, drivers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Drivers> driversList = driversRepository.findAll();
        assertThat(driversList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
