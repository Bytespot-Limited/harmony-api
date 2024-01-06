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
import tech.bytespot.harmony.domain.Guardians;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.domain.enumeration.GuardianRelationshipType;
import tech.bytespot.harmony.repository.GuardiansRepository;
import tech.bytespot.harmony.service.dto.GuardiansDTO;
import tech.bytespot.harmony.service.mapper.GuardiansMapper;

/**
 * Integration tests for the {@link GuardiansResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GuardiansResourceIT {

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

    private static final GuardianRelationshipType DEFAULT_GUARDIAN_TYPE = GuardianRelationshipType.FATHER;
    private static final GuardianRelationshipType UPDATED_GUARDIAN_TYPE = GuardianRelationshipType.MOTHER;

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/guardians";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuardiansRepository guardiansRepository;

    @Autowired
    private GuardiansMapper guardiansMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuardiansMockMvc;

    private Guardians guardians;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guardians createEntity(EntityManager em) {
        Guardians guardians = new Guardians()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .dob(DEFAULT_DOB)
            .nationalId(DEFAULT_NATIONAL_ID)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .guardianType(DEFAULT_GUARDIAN_TYPE)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return guardians;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guardians createUpdatedEntity(EntityManager em) {
        Guardians guardians = new Guardians()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return guardians;
    }

    @BeforeEach
    public void initTest() {
        guardians = createEntity(em);
    }

    @Test
    @Transactional
    void createGuardians() throws Exception {
        int databaseSizeBeforeCreate = guardiansRepository.findAll().size();
        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);
        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isCreated());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeCreate + 1);
        Guardians testGuardians = guardiansList.get(guardiansList.size() - 1);
        assertThat(testGuardians.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testGuardians.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGuardians.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testGuardians.getNationalId()).isEqualTo(DEFAULT_NATIONAL_ID);
        assertThat(testGuardians.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testGuardians.getGuardianType()).isEqualTo(DEFAULT_GUARDIAN_TYPE);
        assertThat(testGuardians.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testGuardians.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testGuardians.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testGuardians.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testGuardians.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createGuardiansWithExistingId() throws Exception {
        // Create the Guardians with an existing ID
        guardians.setId(1L);
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        int databaseSizeBeforeCreate = guardiansRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardiansRepository.findAll().size();
        // set the field null
        guardians.setName(null);

        // Create the Guardians, which fails.
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardiansRepository.findAll().size();
        // set the field null
        guardians.setDob(null);

        // Create the Guardians, which fails.
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNationalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardiansRepository.findAll().size();
        // set the field null
        guardians.setNationalId(null);

        // Create the Guardians, which fails.
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGuardianTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardiansRepository.findAll().size();
        // set the field null
        guardians.setGuardianType(null);

        // Create the Guardians, which fails.
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardiansRepository.findAll().size();
        // set the field null
        guardians.setEmailAddress(null);

        // Create the Guardians, which fails.
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardiansRepository.findAll().size();
        // set the field null
        guardians.setPhoneNumber(null);

        // Create the Guardians, which fails.
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        restGuardiansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isBadRequest());

        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGuardians() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList
        restGuardiansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guardians.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].guardianType").value(hasItem(DEFAULT_GUARDIAN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getGuardians() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get the guardians
        restGuardiansMockMvc
            .perform(get(ENTITY_API_URL_ID, guardians.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guardians.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.nationalId").value(DEFAULT_NATIONAL_ID))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL))
            .andExpect(jsonPath("$.guardianType").value(DEFAULT_GUARDIAN_TYPE.toString()))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getGuardiansByIdFiltering() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        Long id = guardians.getId();

        defaultGuardiansShouldBeFound("id.equals=" + id);
        defaultGuardiansShouldNotBeFound("id.notEquals=" + id);

        defaultGuardiansShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGuardiansShouldNotBeFound("id.greaterThan=" + id);

        defaultGuardiansShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGuardiansShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId equals to DEFAULT_USER_ID
        defaultGuardiansShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the guardiansList where userId equals to UPDATED_USER_ID
        defaultGuardiansShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultGuardiansShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the guardiansList where userId equals to UPDATED_USER_ID
        defaultGuardiansShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId is not null
        defaultGuardiansShouldBeFound("userId.specified=true");

        // Get all the guardiansList where userId is null
        defaultGuardiansShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId is greater than or equal to DEFAULT_USER_ID
        defaultGuardiansShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the guardiansList where userId is greater than or equal to UPDATED_USER_ID
        defaultGuardiansShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId is less than or equal to DEFAULT_USER_ID
        defaultGuardiansShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the guardiansList where userId is less than or equal to SMALLER_USER_ID
        defaultGuardiansShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId is less than DEFAULT_USER_ID
        defaultGuardiansShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the guardiansList where userId is less than UPDATED_USER_ID
        defaultGuardiansShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where userId is greater than DEFAULT_USER_ID
        defaultGuardiansShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the guardiansList where userId is greater than SMALLER_USER_ID
        defaultGuardiansShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where name equals to DEFAULT_NAME
        defaultGuardiansShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the guardiansList where name equals to UPDATED_NAME
        defaultGuardiansShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGuardiansByNameIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGuardiansShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the guardiansList where name equals to UPDATED_NAME
        defaultGuardiansShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGuardiansByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where name is not null
        defaultGuardiansShouldBeFound("name.specified=true");

        // Get all the guardiansList where name is null
        defaultGuardiansShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByNameContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where name contains DEFAULT_NAME
        defaultGuardiansShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the guardiansList where name contains UPDATED_NAME
        defaultGuardiansShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGuardiansByNameNotContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where name does not contain DEFAULT_NAME
        defaultGuardiansShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the guardiansList where name does not contain UPDATED_NAME
        defaultGuardiansShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob equals to DEFAULT_DOB
        defaultGuardiansShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the guardiansList where dob equals to UPDATED_DOB
        defaultGuardiansShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultGuardiansShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the guardiansList where dob equals to UPDATED_DOB
        defaultGuardiansShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob is not null
        defaultGuardiansShouldBeFound("dob.specified=true");

        // Get all the guardiansList where dob is null
        defaultGuardiansShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob is greater than or equal to DEFAULT_DOB
        defaultGuardiansShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the guardiansList where dob is greater than or equal to UPDATED_DOB
        defaultGuardiansShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob is less than or equal to DEFAULT_DOB
        defaultGuardiansShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the guardiansList where dob is less than or equal to SMALLER_DOB
        defaultGuardiansShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob is less than DEFAULT_DOB
        defaultGuardiansShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the guardiansList where dob is less than UPDATED_DOB
        defaultGuardiansShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllGuardiansByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where dob is greater than DEFAULT_DOB
        defaultGuardiansShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the guardiansList where dob is greater than SMALLER_DOB
        defaultGuardiansShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllGuardiansByNationalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where nationalId equals to DEFAULT_NATIONAL_ID
        defaultGuardiansShouldBeFound("nationalId.equals=" + DEFAULT_NATIONAL_ID);

        // Get all the guardiansList where nationalId equals to UPDATED_NATIONAL_ID
        defaultGuardiansShouldNotBeFound("nationalId.equals=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByNationalIdIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where nationalId in DEFAULT_NATIONAL_ID or UPDATED_NATIONAL_ID
        defaultGuardiansShouldBeFound("nationalId.in=" + DEFAULT_NATIONAL_ID + "," + UPDATED_NATIONAL_ID);

        // Get all the guardiansList where nationalId equals to UPDATED_NATIONAL_ID
        defaultGuardiansShouldNotBeFound("nationalId.in=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByNationalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where nationalId is not null
        defaultGuardiansShouldBeFound("nationalId.specified=true");

        // Get all the guardiansList where nationalId is null
        defaultGuardiansShouldNotBeFound("nationalId.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByNationalIdContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where nationalId contains DEFAULT_NATIONAL_ID
        defaultGuardiansShouldBeFound("nationalId.contains=" + DEFAULT_NATIONAL_ID);

        // Get all the guardiansList where nationalId contains UPDATED_NATIONAL_ID
        defaultGuardiansShouldNotBeFound("nationalId.contains=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByNationalIdNotContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where nationalId does not contain DEFAULT_NATIONAL_ID
        defaultGuardiansShouldNotBeFound("nationalId.doesNotContain=" + DEFAULT_NATIONAL_ID);

        // Get all the guardiansList where nationalId does not contain UPDATED_NATIONAL_ID
        defaultGuardiansShouldBeFound("nationalId.doesNotContain=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllGuardiansByProfileImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where profileImageUrl equals to DEFAULT_PROFILE_IMAGE_URL
        defaultGuardiansShouldBeFound("profileImageUrl.equals=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the guardiansList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultGuardiansShouldNotBeFound("profileImageUrl.equals=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllGuardiansByProfileImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where profileImageUrl in DEFAULT_PROFILE_IMAGE_URL or UPDATED_PROFILE_IMAGE_URL
        defaultGuardiansShouldBeFound("profileImageUrl.in=" + DEFAULT_PROFILE_IMAGE_URL + "," + UPDATED_PROFILE_IMAGE_URL);

        // Get all the guardiansList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultGuardiansShouldNotBeFound("profileImageUrl.in=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllGuardiansByProfileImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where profileImageUrl is not null
        defaultGuardiansShouldBeFound("profileImageUrl.specified=true");

        // Get all the guardiansList where profileImageUrl is null
        defaultGuardiansShouldNotBeFound("profileImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByProfileImageUrlContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where profileImageUrl contains DEFAULT_PROFILE_IMAGE_URL
        defaultGuardiansShouldBeFound("profileImageUrl.contains=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the guardiansList where profileImageUrl contains UPDATED_PROFILE_IMAGE_URL
        defaultGuardiansShouldNotBeFound("profileImageUrl.contains=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllGuardiansByProfileImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where profileImageUrl does not contain DEFAULT_PROFILE_IMAGE_URL
        defaultGuardiansShouldNotBeFound("profileImageUrl.doesNotContain=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the guardiansList where profileImageUrl does not contain UPDATED_PROFILE_IMAGE_URL
        defaultGuardiansShouldBeFound("profileImageUrl.doesNotContain=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllGuardiansByGuardianTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where guardianType equals to DEFAULT_GUARDIAN_TYPE
        defaultGuardiansShouldBeFound("guardianType.equals=" + DEFAULT_GUARDIAN_TYPE);

        // Get all the guardiansList where guardianType equals to UPDATED_GUARDIAN_TYPE
        defaultGuardiansShouldNotBeFound("guardianType.equals=" + UPDATED_GUARDIAN_TYPE);
    }

    @Test
    @Transactional
    void getAllGuardiansByGuardianTypeIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where guardianType in DEFAULT_GUARDIAN_TYPE or UPDATED_GUARDIAN_TYPE
        defaultGuardiansShouldBeFound("guardianType.in=" + DEFAULT_GUARDIAN_TYPE + "," + UPDATED_GUARDIAN_TYPE);

        // Get all the guardiansList where guardianType equals to UPDATED_GUARDIAN_TYPE
        defaultGuardiansShouldNotBeFound("guardianType.in=" + UPDATED_GUARDIAN_TYPE);
    }

    @Test
    @Transactional
    void getAllGuardiansByGuardianTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where guardianType is not null
        defaultGuardiansShouldBeFound("guardianType.specified=true");

        // Get all the guardiansList where guardianType is null
        defaultGuardiansShouldNotBeFound("guardianType.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultGuardiansShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the guardiansList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultGuardiansShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGuardiansByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultGuardiansShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the guardiansList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultGuardiansShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGuardiansByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where emailAddress is not null
        defaultGuardiansShouldBeFound("emailAddress.specified=true");

        // Get all the guardiansList where emailAddress is null
        defaultGuardiansShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultGuardiansShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the guardiansList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultGuardiansShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGuardiansByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultGuardiansShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the guardiansList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultGuardiansShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGuardiansByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultGuardiansShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the guardiansList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultGuardiansShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGuardiansByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultGuardiansShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the guardiansList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultGuardiansShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGuardiansByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where phoneNumber is not null
        defaultGuardiansShouldBeFound("phoneNumber.specified=true");

        // Get all the guardiansList where phoneNumber is null
        defaultGuardiansShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultGuardiansShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the guardiansList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultGuardiansShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGuardiansByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultGuardiansShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the guardiansList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultGuardiansShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGuardiansByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultGuardiansShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the guardiansList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultGuardiansShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllGuardiansByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultGuardiansShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the guardiansList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultGuardiansShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllGuardiansByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where entityStatus is not null
        defaultGuardiansShouldBeFound("entityStatus.specified=true");

        // Get all the guardiansList where entityStatus is null
        defaultGuardiansShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where creationDate equals to DEFAULT_CREATION_DATE
        defaultGuardiansShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the guardiansList where creationDate equals to UPDATED_CREATION_DATE
        defaultGuardiansShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllGuardiansByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultGuardiansShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the guardiansList where creationDate equals to UPDATED_CREATION_DATE
        defaultGuardiansShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllGuardiansByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where creationDate is not null
        defaultGuardiansShouldBeFound("creationDate.specified=true");

        // Get all the guardiansList where creationDate is null
        defaultGuardiansShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultGuardiansShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the guardiansList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultGuardiansShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGuardiansByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultGuardiansShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the guardiansList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultGuardiansShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGuardiansByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        // Get all the guardiansList where modifiedDate is not null
        defaultGuardiansShouldBeFound("modifiedDate.specified=true");

        // Get all the guardiansList where modifiedDate is null
        defaultGuardiansShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGuardiansByStudentsIsEqualToSomething() throws Exception {
        Students students;
        if (TestUtil.findAll(em, Students.class).isEmpty()) {
            guardiansRepository.saveAndFlush(guardians);
            students = StudentsResourceIT.createEntity(em);
        } else {
            students = TestUtil.findAll(em, Students.class).get(0);
        }
        em.persist(students);
        em.flush();
        guardians.addStudents(students);
        guardiansRepository.saveAndFlush(guardians);
        Long studentsId = students.getId();
        // Get all the guardiansList where students equals to studentsId
        defaultGuardiansShouldBeFound("studentsId.equals=" + studentsId);

        // Get all the guardiansList where students equals to (studentsId + 1)
        defaultGuardiansShouldNotBeFound("studentsId.equals=" + (studentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuardiansShouldBeFound(String filter) throws Exception {
        restGuardiansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guardians.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].guardianType").value(hasItem(DEFAULT_GUARDIAN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restGuardiansMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuardiansShouldNotBeFound(String filter) throws Exception {
        restGuardiansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuardiansMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGuardians() throws Exception {
        // Get the guardians
        restGuardiansMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGuardians() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();

        // Update the guardians
        Guardians updatedGuardians = guardiansRepository.findById(guardians.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGuardians are not directly saved in db
        em.detach(updatedGuardians);
        updatedGuardians
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(updatedGuardians);

        restGuardiansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardiansDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardiansDTO))
            )
            .andExpect(status().isOk());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
        Guardians testGuardians = guardiansList.get(guardiansList.size() - 1);
        assertThat(testGuardians.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testGuardians.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGuardians.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testGuardians.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testGuardians.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testGuardians.getGuardianType()).isEqualTo(UPDATED_GUARDIAN_TYPE);
        assertThat(testGuardians.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testGuardians.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGuardians.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testGuardians.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testGuardians.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingGuardians() throws Exception {
        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();
        guardians.setId(count.incrementAndGet());

        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardiansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardiansDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardiansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuardians() throws Exception {
        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();
        guardians.setId(count.incrementAndGet());

        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardiansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardiansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuardians() throws Exception {
        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();
        guardians.setId(count.incrementAndGet());

        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardiansMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardiansDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGuardiansWithPatch() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();

        // Update the guardians using partial update
        Guardians partialUpdatedGuardians = new Guardians();
        partialUpdatedGuardians.setId(guardians.getId());

        partialUpdatedGuardians
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .creationDate(UPDATED_CREATION_DATE);

        restGuardiansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuardians.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuardians))
            )
            .andExpect(status().isOk());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
        Guardians testGuardians = guardiansList.get(guardiansList.size() - 1);
        assertThat(testGuardians.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testGuardians.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGuardians.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testGuardians.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testGuardians.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testGuardians.getGuardianType()).isEqualTo(UPDATED_GUARDIAN_TYPE);
        assertThat(testGuardians.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testGuardians.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testGuardians.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testGuardians.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testGuardians.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateGuardiansWithPatch() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();

        // Update the guardians using partial update
        Guardians partialUpdatedGuardians = new Guardians();
        partialUpdatedGuardians.setId(guardians.getId());

        partialUpdatedGuardians
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .guardianType(UPDATED_GUARDIAN_TYPE)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restGuardiansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuardians.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuardians))
            )
            .andExpect(status().isOk());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
        Guardians testGuardians = guardiansList.get(guardiansList.size() - 1);
        assertThat(testGuardians.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testGuardians.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGuardians.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testGuardians.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testGuardians.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testGuardians.getGuardianType()).isEqualTo(UPDATED_GUARDIAN_TYPE);
        assertThat(testGuardians.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testGuardians.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGuardians.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testGuardians.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testGuardians.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingGuardians() throws Exception {
        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();
        guardians.setId(count.incrementAndGet());

        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardiansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guardiansDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardiansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuardians() throws Exception {
        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();
        guardians.setId(count.incrementAndGet());

        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardiansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardiansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuardians() throws Exception {
        int databaseSizeBeforeUpdate = guardiansRepository.findAll().size();
        guardians.setId(count.incrementAndGet());

        // Create the Guardians
        GuardiansDTO guardiansDTO = guardiansMapper.toDto(guardians);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardiansMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(guardiansDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guardians in the database
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGuardians() throws Exception {
        // Initialize the database
        guardiansRepository.saveAndFlush(guardians);

        int databaseSizeBeforeDelete = guardiansRepository.findAll().size();

        // Delete the guardians
        restGuardiansMockMvc
            .perform(delete(ENTITY_API_URL_ID, guardians.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guardians> guardiansList = guardiansRepository.findAll();
        assertThat(guardiansList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
