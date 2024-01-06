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
import tech.bytespot.harmony.domain.SchoolStaff;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.repository.SchoolStaffRepository;
import tech.bytespot.harmony.service.dto.SchoolStaffDTO;
import tech.bytespot.harmony.service.mapper.SchoolStaffMapper;

/**
 * Integration tests for the {@link SchoolStaffResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchoolStaffResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;
    private static final Integer SMALLER_USER_ID = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_DESCRIPTION = "BBBBBBBBBB";

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

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/school-staffs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolStaffRepository schoolStaffRepository;

    @Autowired
    private SchoolStaffMapper schoolStaffMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolStaffMockMvc;

    private SchoolStaff schoolStaff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolStaff createEntity(EntityManager em) {
        SchoolStaff schoolStaff = new SchoolStaff()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .roleDescription(DEFAULT_ROLE_DESCRIPTION)
            .dob(DEFAULT_DOB)
            .nationalId(DEFAULT_NATIONAL_ID)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return schoolStaff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolStaff createUpdatedEntity(EntityManager em) {
        SchoolStaff schoolStaff = new SchoolStaff()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .roleDescription(UPDATED_ROLE_DESCRIPTION)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return schoolStaff;
    }

    @BeforeEach
    public void initTest() {
        schoolStaff = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolStaff() throws Exception {
        int databaseSizeBeforeCreate = schoolStaffRepository.findAll().size();
        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);
        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolStaff testSchoolStaff = schoolStaffList.get(schoolStaffList.size() - 1);
        assertThat(testSchoolStaff.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testSchoolStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSchoolStaff.getRoleDescription()).isEqualTo(DEFAULT_ROLE_DESCRIPTION);
        assertThat(testSchoolStaff.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testSchoolStaff.getNationalId()).isEqualTo(DEFAULT_NATIONAL_ID);
        assertThat(testSchoolStaff.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testSchoolStaff.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testSchoolStaff.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSchoolStaff.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testSchoolStaff.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSchoolStaff.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createSchoolStaffWithExistingId() throws Exception {
        // Create the SchoolStaff with an existing ID
        schoolStaff.setId(1L);
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        int databaseSizeBeforeCreate = schoolStaffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setUserId(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setName(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setRoleDescription(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setDob(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNationalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setNationalId(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setEmailAddress(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolStaffRepository.findAll().size();
        // set the field null
        schoolStaff.setPhoneNumber(null);

        // Create the SchoolStaff, which fails.
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        restSchoolStaffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolStaffs() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList
        restSchoolStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolStaff.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].roleDescription").value(hasItem(DEFAULT_ROLE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSchoolStaff() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get the schoolStaff
        restSchoolStaffMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolStaff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolStaff.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.roleDescription").value(DEFAULT_ROLE_DESCRIPTION))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.nationalId").value(DEFAULT_NATIONAL_ID))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolStaffsByIdFiltering() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        Long id = schoolStaff.getId();

        defaultSchoolStaffShouldBeFound("id.equals=" + id);
        defaultSchoolStaffShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolStaffShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolStaffShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolStaffShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolStaffShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId equals to DEFAULT_USER_ID
        defaultSchoolStaffShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the schoolStaffList where userId equals to UPDATED_USER_ID
        defaultSchoolStaffShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultSchoolStaffShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the schoolStaffList where userId equals to UPDATED_USER_ID
        defaultSchoolStaffShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId is not null
        defaultSchoolStaffShouldBeFound("userId.specified=true");

        // Get all the schoolStaffList where userId is null
        defaultSchoolStaffShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId is greater than or equal to DEFAULT_USER_ID
        defaultSchoolStaffShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the schoolStaffList where userId is greater than or equal to UPDATED_USER_ID
        defaultSchoolStaffShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId is less than or equal to DEFAULT_USER_ID
        defaultSchoolStaffShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the schoolStaffList where userId is less than or equal to SMALLER_USER_ID
        defaultSchoolStaffShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId is less than DEFAULT_USER_ID
        defaultSchoolStaffShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the schoolStaffList where userId is less than UPDATED_USER_ID
        defaultSchoolStaffShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where userId is greater than DEFAULT_USER_ID
        defaultSchoolStaffShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the schoolStaffList where userId is greater than SMALLER_USER_ID
        defaultSchoolStaffShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where name equals to DEFAULT_NAME
        defaultSchoolStaffShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the schoolStaffList where name equals to UPDATED_NAME
        defaultSchoolStaffShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSchoolStaffShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the schoolStaffList where name equals to UPDATED_NAME
        defaultSchoolStaffShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where name is not null
        defaultSchoolStaffShouldBeFound("name.specified=true");

        // Get all the schoolStaffList where name is null
        defaultSchoolStaffShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNameContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where name contains DEFAULT_NAME
        defaultSchoolStaffShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the schoolStaffList where name contains UPDATED_NAME
        defaultSchoolStaffShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where name does not contain DEFAULT_NAME
        defaultSchoolStaffShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the schoolStaffList where name does not contain UPDATED_NAME
        defaultSchoolStaffShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByRoleDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where roleDescription equals to DEFAULT_ROLE_DESCRIPTION
        defaultSchoolStaffShouldBeFound("roleDescription.equals=" + DEFAULT_ROLE_DESCRIPTION);

        // Get all the schoolStaffList where roleDescription equals to UPDATED_ROLE_DESCRIPTION
        defaultSchoolStaffShouldNotBeFound("roleDescription.equals=" + UPDATED_ROLE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByRoleDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where roleDescription in DEFAULT_ROLE_DESCRIPTION or UPDATED_ROLE_DESCRIPTION
        defaultSchoolStaffShouldBeFound("roleDescription.in=" + DEFAULT_ROLE_DESCRIPTION + "," + UPDATED_ROLE_DESCRIPTION);

        // Get all the schoolStaffList where roleDescription equals to UPDATED_ROLE_DESCRIPTION
        defaultSchoolStaffShouldNotBeFound("roleDescription.in=" + UPDATED_ROLE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByRoleDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where roleDescription is not null
        defaultSchoolStaffShouldBeFound("roleDescription.specified=true");

        // Get all the schoolStaffList where roleDescription is null
        defaultSchoolStaffShouldNotBeFound("roleDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByRoleDescriptionContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where roleDescription contains DEFAULT_ROLE_DESCRIPTION
        defaultSchoolStaffShouldBeFound("roleDescription.contains=" + DEFAULT_ROLE_DESCRIPTION);

        // Get all the schoolStaffList where roleDescription contains UPDATED_ROLE_DESCRIPTION
        defaultSchoolStaffShouldNotBeFound("roleDescription.contains=" + UPDATED_ROLE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByRoleDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where roleDescription does not contain DEFAULT_ROLE_DESCRIPTION
        defaultSchoolStaffShouldNotBeFound("roleDescription.doesNotContain=" + DEFAULT_ROLE_DESCRIPTION);

        // Get all the schoolStaffList where roleDescription does not contain UPDATED_ROLE_DESCRIPTION
        defaultSchoolStaffShouldBeFound("roleDescription.doesNotContain=" + UPDATED_ROLE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob equals to DEFAULT_DOB
        defaultSchoolStaffShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the schoolStaffList where dob equals to UPDATED_DOB
        defaultSchoolStaffShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultSchoolStaffShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the schoolStaffList where dob equals to UPDATED_DOB
        defaultSchoolStaffShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob is not null
        defaultSchoolStaffShouldBeFound("dob.specified=true");

        // Get all the schoolStaffList where dob is null
        defaultSchoolStaffShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob is greater than or equal to DEFAULT_DOB
        defaultSchoolStaffShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the schoolStaffList where dob is greater than or equal to UPDATED_DOB
        defaultSchoolStaffShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob is less than or equal to DEFAULT_DOB
        defaultSchoolStaffShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the schoolStaffList where dob is less than or equal to SMALLER_DOB
        defaultSchoolStaffShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob is less than DEFAULT_DOB
        defaultSchoolStaffShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the schoolStaffList where dob is less than UPDATED_DOB
        defaultSchoolStaffShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where dob is greater than DEFAULT_DOB
        defaultSchoolStaffShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the schoolStaffList where dob is greater than SMALLER_DOB
        defaultSchoolStaffShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNationalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where nationalId equals to DEFAULT_NATIONAL_ID
        defaultSchoolStaffShouldBeFound("nationalId.equals=" + DEFAULT_NATIONAL_ID);

        // Get all the schoolStaffList where nationalId equals to UPDATED_NATIONAL_ID
        defaultSchoolStaffShouldNotBeFound("nationalId.equals=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNationalIdIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where nationalId in DEFAULT_NATIONAL_ID or UPDATED_NATIONAL_ID
        defaultSchoolStaffShouldBeFound("nationalId.in=" + DEFAULT_NATIONAL_ID + "," + UPDATED_NATIONAL_ID);

        // Get all the schoolStaffList where nationalId equals to UPDATED_NATIONAL_ID
        defaultSchoolStaffShouldNotBeFound("nationalId.in=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNationalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where nationalId is not null
        defaultSchoolStaffShouldBeFound("nationalId.specified=true");

        // Get all the schoolStaffList where nationalId is null
        defaultSchoolStaffShouldNotBeFound("nationalId.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNationalIdContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where nationalId contains DEFAULT_NATIONAL_ID
        defaultSchoolStaffShouldBeFound("nationalId.contains=" + DEFAULT_NATIONAL_ID);

        // Get all the schoolStaffList where nationalId contains UPDATED_NATIONAL_ID
        defaultSchoolStaffShouldNotBeFound("nationalId.contains=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByNationalIdNotContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where nationalId does not contain DEFAULT_NATIONAL_ID
        defaultSchoolStaffShouldNotBeFound("nationalId.doesNotContain=" + DEFAULT_NATIONAL_ID);

        // Get all the schoolStaffList where nationalId does not contain UPDATED_NATIONAL_ID
        defaultSchoolStaffShouldBeFound("nationalId.doesNotContain=" + UPDATED_NATIONAL_ID);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByProfileImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where profileImageUrl equals to DEFAULT_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldBeFound("profileImageUrl.equals=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the schoolStaffList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldNotBeFound("profileImageUrl.equals=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByProfileImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where profileImageUrl in DEFAULT_PROFILE_IMAGE_URL or UPDATED_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldBeFound("profileImageUrl.in=" + DEFAULT_PROFILE_IMAGE_URL + "," + UPDATED_PROFILE_IMAGE_URL);

        // Get all the schoolStaffList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldNotBeFound("profileImageUrl.in=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByProfileImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where profileImageUrl is not null
        defaultSchoolStaffShouldBeFound("profileImageUrl.specified=true");

        // Get all the schoolStaffList where profileImageUrl is null
        defaultSchoolStaffShouldNotBeFound("profileImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByProfileImageUrlContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where profileImageUrl contains DEFAULT_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldBeFound("profileImageUrl.contains=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the schoolStaffList where profileImageUrl contains UPDATED_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldNotBeFound("profileImageUrl.contains=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByProfileImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where profileImageUrl does not contain DEFAULT_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldNotBeFound("profileImageUrl.doesNotContain=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the schoolStaffList where profileImageUrl does not contain UPDATED_PROFILE_IMAGE_URL
        defaultSchoolStaffShouldBeFound("profileImageUrl.doesNotContain=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultSchoolStaffShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the schoolStaffList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultSchoolStaffShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultSchoolStaffShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the schoolStaffList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultSchoolStaffShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where emailAddress is not null
        defaultSchoolStaffShouldBeFound("emailAddress.specified=true");

        // Get all the schoolStaffList where emailAddress is null
        defaultSchoolStaffShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultSchoolStaffShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the schoolStaffList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultSchoolStaffShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultSchoolStaffShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the schoolStaffList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultSchoolStaffShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultSchoolStaffShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the schoolStaffList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSchoolStaffShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultSchoolStaffShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the schoolStaffList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSchoolStaffShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where phoneNumber is not null
        defaultSchoolStaffShouldBeFound("phoneNumber.specified=true");

        // Get all the schoolStaffList where phoneNumber is null
        defaultSchoolStaffShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultSchoolStaffShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the schoolStaffList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultSchoolStaffShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultSchoolStaffShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the schoolStaffList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultSchoolStaffShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultSchoolStaffShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the schoolStaffList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultSchoolStaffShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultSchoolStaffShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the schoolStaffList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultSchoolStaffShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where entityStatus is not null
        defaultSchoolStaffShouldBeFound("entityStatus.specified=true");

        // Get all the schoolStaffList where entityStatus is null
        defaultSchoolStaffShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where creationDate equals to DEFAULT_CREATION_DATE
        defaultSchoolStaffShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the schoolStaffList where creationDate equals to UPDATED_CREATION_DATE
        defaultSchoolStaffShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultSchoolStaffShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the schoolStaffList where creationDate equals to UPDATED_CREATION_DATE
        defaultSchoolStaffShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where creationDate is not null
        defaultSchoolStaffShouldBeFound("creationDate.specified=true");

        // Get all the schoolStaffList where creationDate is null
        defaultSchoolStaffShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultSchoolStaffShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the schoolStaffList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultSchoolStaffShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultSchoolStaffShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the schoolStaffList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultSchoolStaffShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolStaffsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        // Get all the schoolStaffList where modifiedDate is not null
        defaultSchoolStaffShouldBeFound("modifiedDate.specified=true");

        // Get all the schoolStaffList where modifiedDate is null
        defaultSchoolStaffShouldNotBeFound("modifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolStaffShouldBeFound(String filter) throws Exception {
        restSchoolStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolStaff.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].roleDescription").value(hasItem(DEFAULT_ROLE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationalId").value(hasItem(DEFAULT_NATIONAL_ID)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolStaffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolStaffShouldNotBeFound(String filter) throws Exception {
        restSchoolStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolStaffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolStaff() throws Exception {
        // Get the schoolStaff
        restSchoolStaffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchoolStaff() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();

        // Update the schoolStaff
        SchoolStaff updatedSchoolStaff = schoolStaffRepository.findById(schoolStaff.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchoolStaff are not directly saved in db
        em.detach(updatedSchoolStaff);
        updatedSchoolStaff
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .roleDescription(UPDATED_ROLE_DESCRIPTION)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(updatedSchoolStaff);

        restSchoolStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolStaffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
        SchoolStaff testSchoolStaff = schoolStaffList.get(schoolStaffList.size() - 1);
        assertThat(testSchoolStaff.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSchoolStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchoolStaff.getRoleDescription()).isEqualTo(UPDATED_ROLE_DESCRIPTION);
        assertThat(testSchoolStaff.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testSchoolStaff.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testSchoolStaff.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testSchoolStaff.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSchoolStaff.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSchoolStaff.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testSchoolStaff.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSchoolStaff.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolStaff() throws Exception {
        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();
        schoolStaff.setId(count.incrementAndGet());

        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolStaffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolStaff() throws Exception {
        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();
        schoolStaff.setId(count.incrementAndGet());

        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolStaff() throws Exception {
        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();
        schoolStaff.setId(count.incrementAndGet());

        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolStaffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolStaffWithPatch() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();

        // Update the schoolStaff using partial update
        SchoolStaff partialUpdatedSchoolStaff = new SchoolStaff();
        partialUpdatedSchoolStaff.setId(schoolStaff.getId());

        partialUpdatedSchoolStaff
            .userId(UPDATED_USER_ID)
            .dob(UPDATED_DOB)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSchoolStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolStaff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolStaff))
            )
            .andExpect(status().isOk());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
        SchoolStaff testSchoolStaff = schoolStaffList.get(schoolStaffList.size() - 1);
        assertThat(testSchoolStaff.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSchoolStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSchoolStaff.getRoleDescription()).isEqualTo(DEFAULT_ROLE_DESCRIPTION);
        assertThat(testSchoolStaff.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testSchoolStaff.getNationalId()).isEqualTo(DEFAULT_NATIONAL_ID);
        assertThat(testSchoolStaff.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testSchoolStaff.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testSchoolStaff.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSchoolStaff.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testSchoolStaff.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSchoolStaff.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolStaffWithPatch() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();

        // Update the schoolStaff using partial update
        SchoolStaff partialUpdatedSchoolStaff = new SchoolStaff();
        partialUpdatedSchoolStaff.setId(schoolStaff.getId());

        partialUpdatedSchoolStaff
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .roleDescription(UPDATED_ROLE_DESCRIPTION)
            .dob(UPDATED_DOB)
            .nationalId(UPDATED_NATIONAL_ID)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSchoolStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolStaff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolStaff))
            )
            .andExpect(status().isOk());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
        SchoolStaff testSchoolStaff = schoolStaffList.get(schoolStaffList.size() - 1);
        assertThat(testSchoolStaff.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSchoolStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchoolStaff.getRoleDescription()).isEqualTo(UPDATED_ROLE_DESCRIPTION);
        assertThat(testSchoolStaff.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testSchoolStaff.getNationalId()).isEqualTo(UPDATED_NATIONAL_ID);
        assertThat(testSchoolStaff.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testSchoolStaff.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSchoolStaff.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSchoolStaff.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testSchoolStaff.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSchoolStaff.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolStaff() throws Exception {
        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();
        schoolStaff.setId(count.incrementAndGet());

        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolStaffDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolStaff() throws Exception {
        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();
        schoolStaff.setId(count.incrementAndGet());

        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolStaff() throws Exception {
        int databaseSizeBeforeUpdate = schoolStaffRepository.findAll().size();
        schoolStaff.setId(count.incrementAndGet());

        // Create the SchoolStaff
        SchoolStaffDTO schoolStaffDTO = schoolStaffMapper.toDto(schoolStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolStaffMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schoolStaffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolStaff in the database
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolStaff() throws Exception {
        // Initialize the database
        schoolStaffRepository.saveAndFlush(schoolStaff);

        int databaseSizeBeforeDelete = schoolStaffRepository.findAll().size();

        // Delete the schoolStaff
        restSchoolStaffMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolStaff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolStaff> schoolStaffList = schoolStaffRepository.findAll();
        assertThat(schoolStaffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
