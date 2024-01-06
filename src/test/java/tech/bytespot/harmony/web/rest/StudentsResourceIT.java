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
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.domain.Guardians;
import tech.bytespot.harmony.domain.StudentBillings;
import tech.bytespot.harmony.domain.StudentTrips;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.domain.enumeration.BillingStatus;
import tech.bytespot.harmony.domain.enumeration.ClassType;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.repository.StudentsRepository;
import tech.bytespot.harmony.service.dto.StudentsDTO;
import tech.bytespot.harmony.service.mapper.StudentsMapper;

/**
 * Integration tests for the {@link StudentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final ClassType DEFAULT_CLASS_LEVEL = ClassType.GRADE_1;
    private static final ClassType UPDATED_CLASS_LEVEL = ClassType.GRADE_2;

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_HOME_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final BillingStatus DEFAULT_BILLING_STATUS = BillingStatus.ACTIVE;
    private static final BillingStatus UPDATED_BILLING_STATUS = BillingStatus.OVERDUE;

    private static final Instant DEFAULT_NEXT_BILLING_CYCLE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_BILLING_CYCLE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentsMockMvc;

    private Students students;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Students createEntity(EntityManager em) {
        Students students = new Students()
            .name(DEFAULT_NAME)
            .dob(DEFAULT_DOB)
            .classLevel(DEFAULT_CLASS_LEVEL)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .homeAddress(DEFAULT_HOME_ADDRESS)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .billingStatus(DEFAULT_BILLING_STATUS)
            .nextBillingCycle(DEFAULT_NEXT_BILLING_CYCLE)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return students;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Students createUpdatedEntity(EntityManager em) {
        Students students = new Students()
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .classLevel(UPDATED_CLASS_LEVEL)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .homeAddress(UPDATED_HOME_ADDRESS)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .billingStatus(UPDATED_BILLING_STATUS)
            .nextBillingCycle(UPDATED_NEXT_BILLING_CYCLE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return students;
    }

    @BeforeEach
    public void initTest() {
        students = createEntity(em);
    }

    @Test
    @Transactional
    void createStudents() throws Exception {
        int databaseSizeBeforeCreate = studentsRepository.findAll().size();
        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);
        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeCreate + 1);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudents.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testStudents.getClassLevel()).isEqualTo(DEFAULT_CLASS_LEVEL);
        assertThat(testStudents.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testStudents.getHomeAddress()).isEqualTo(DEFAULT_HOME_ADDRESS);
        assertThat(testStudents.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testStudents.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testStudents.getBillingStatus()).isEqualTo(DEFAULT_BILLING_STATUS);
        assertThat(testStudents.getNextBillingCycle()).isEqualTo(DEFAULT_NEXT_BILLING_CYCLE);
        assertThat(testStudents.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testStudents.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testStudents.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createStudentsWithExistingId() throws Exception {
        // Create the Students with an existing ID
        students.setId(1L);
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        int databaseSizeBeforeCreate = studentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().size();
        // set the field null
        students.setName(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().size();
        // set the field null
        students.setDob(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().size();
        // set the field null
        students.setClassLevel(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHomeAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().size();
        // set the field null
        students.setHomeAddress(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBillingStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().size();
        // set the field null
        students.setBillingStatus(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNextBillingCycleIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().size();
        // set the field null
        students.setNextBillingCycle(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        restStudentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isBadRequest());

        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudents() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList
        restStudentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(students.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].classLevel").value(hasItem(DEFAULT_CLASS_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].homeAddress").value(hasItem(DEFAULT_HOME_ADDRESS)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].billingStatus").value(hasItem(DEFAULT_BILLING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nextBillingCycle").value(hasItem(DEFAULT_NEXT_BILLING_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudents() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get the students
        restStudentsMockMvc
            .perform(get(ENTITY_API_URL_ID, students.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(students.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.classLevel").value(DEFAULT_CLASS_LEVEL.toString()))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL))
            .andExpect(jsonPath("$.homeAddress").value(DEFAULT_HOME_ADDRESS))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.billingStatus").value(DEFAULT_BILLING_STATUS.toString()))
            .andExpect(jsonPath("$.nextBillingCycle").value(DEFAULT_NEXT_BILLING_CYCLE.toString()))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        Long id = students.getId();

        defaultStudentsShouldBeFound("id.equals=" + id);
        defaultStudentsShouldNotBeFound("id.notEquals=" + id);

        defaultStudentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentsShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where name equals to DEFAULT_NAME
        defaultStudentsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the studentsList where name equals to UPDATED_NAME
        defaultStudentsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStudentsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the studentsList where name equals to UPDATED_NAME
        defaultStudentsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where name is not null
        defaultStudentsShouldBeFound("name.specified=true");

        // Get all the studentsList where name is null
        defaultStudentsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByNameContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where name contains DEFAULT_NAME
        defaultStudentsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the studentsList where name contains UPDATED_NAME
        defaultStudentsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where name does not contain DEFAULT_NAME
        defaultStudentsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the studentsList where name does not contain UPDATED_NAME
        defaultStudentsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob equals to DEFAULT_DOB
        defaultStudentsShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the studentsList where dob equals to UPDATED_DOB
        defaultStudentsShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultStudentsShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the studentsList where dob equals to UPDATED_DOB
        defaultStudentsShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob is not null
        defaultStudentsShouldBeFound("dob.specified=true");

        // Get all the studentsList where dob is null
        defaultStudentsShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob is greater than or equal to DEFAULT_DOB
        defaultStudentsShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the studentsList where dob is greater than or equal to UPDATED_DOB
        defaultStudentsShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob is less than or equal to DEFAULT_DOB
        defaultStudentsShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the studentsList where dob is less than or equal to SMALLER_DOB
        defaultStudentsShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob is less than DEFAULT_DOB
        defaultStudentsShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the studentsList where dob is less than UPDATED_DOB
        defaultStudentsShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllStudentsByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where dob is greater than DEFAULT_DOB
        defaultStudentsShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the studentsList where dob is greater than SMALLER_DOB
        defaultStudentsShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllStudentsByClassLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where classLevel equals to DEFAULT_CLASS_LEVEL
        defaultStudentsShouldBeFound("classLevel.equals=" + DEFAULT_CLASS_LEVEL);

        // Get all the studentsList where classLevel equals to UPDATED_CLASS_LEVEL
        defaultStudentsShouldNotBeFound("classLevel.equals=" + UPDATED_CLASS_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByClassLevelIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where classLevel in DEFAULT_CLASS_LEVEL or UPDATED_CLASS_LEVEL
        defaultStudentsShouldBeFound("classLevel.in=" + DEFAULT_CLASS_LEVEL + "," + UPDATED_CLASS_LEVEL);

        // Get all the studentsList where classLevel equals to UPDATED_CLASS_LEVEL
        defaultStudentsShouldNotBeFound("classLevel.in=" + UPDATED_CLASS_LEVEL);
    }

    @Test
    @Transactional
    void getAllStudentsByClassLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where classLevel is not null
        defaultStudentsShouldBeFound("classLevel.specified=true");

        // Get all the studentsList where classLevel is null
        defaultStudentsShouldNotBeFound("classLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByProfileImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where profileImageUrl equals to DEFAULT_PROFILE_IMAGE_URL
        defaultStudentsShouldBeFound("profileImageUrl.equals=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the studentsList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultStudentsShouldNotBeFound("profileImageUrl.equals=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllStudentsByProfileImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where profileImageUrl in DEFAULT_PROFILE_IMAGE_URL or UPDATED_PROFILE_IMAGE_URL
        defaultStudentsShouldBeFound("profileImageUrl.in=" + DEFAULT_PROFILE_IMAGE_URL + "," + UPDATED_PROFILE_IMAGE_URL);

        // Get all the studentsList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultStudentsShouldNotBeFound("profileImageUrl.in=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllStudentsByProfileImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where profileImageUrl is not null
        defaultStudentsShouldBeFound("profileImageUrl.specified=true");

        // Get all the studentsList where profileImageUrl is null
        defaultStudentsShouldNotBeFound("profileImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByProfileImageUrlContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where profileImageUrl contains DEFAULT_PROFILE_IMAGE_URL
        defaultStudentsShouldBeFound("profileImageUrl.contains=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the studentsList where profileImageUrl contains UPDATED_PROFILE_IMAGE_URL
        defaultStudentsShouldNotBeFound("profileImageUrl.contains=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllStudentsByProfileImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where profileImageUrl does not contain DEFAULT_PROFILE_IMAGE_URL
        defaultStudentsShouldNotBeFound("profileImageUrl.doesNotContain=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the studentsList where profileImageUrl does not contain UPDATED_PROFILE_IMAGE_URL
        defaultStudentsShouldBeFound("profileImageUrl.doesNotContain=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllStudentsByHomeAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where homeAddress equals to DEFAULT_HOME_ADDRESS
        defaultStudentsShouldBeFound("homeAddress.equals=" + DEFAULT_HOME_ADDRESS);

        // Get all the studentsList where homeAddress equals to UPDATED_HOME_ADDRESS
        defaultStudentsShouldNotBeFound("homeAddress.equals=" + UPDATED_HOME_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudentsByHomeAddressIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where homeAddress in DEFAULT_HOME_ADDRESS or UPDATED_HOME_ADDRESS
        defaultStudentsShouldBeFound("homeAddress.in=" + DEFAULT_HOME_ADDRESS + "," + UPDATED_HOME_ADDRESS);

        // Get all the studentsList where homeAddress equals to UPDATED_HOME_ADDRESS
        defaultStudentsShouldNotBeFound("homeAddress.in=" + UPDATED_HOME_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudentsByHomeAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where homeAddress is not null
        defaultStudentsShouldBeFound("homeAddress.specified=true");

        // Get all the studentsList where homeAddress is null
        defaultStudentsShouldNotBeFound("homeAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByHomeAddressContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where homeAddress contains DEFAULT_HOME_ADDRESS
        defaultStudentsShouldBeFound("homeAddress.contains=" + DEFAULT_HOME_ADDRESS);

        // Get all the studentsList where homeAddress contains UPDATED_HOME_ADDRESS
        defaultStudentsShouldNotBeFound("homeAddress.contains=" + UPDATED_HOME_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudentsByHomeAddressNotContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where homeAddress does not contain DEFAULT_HOME_ADDRESS
        defaultStudentsShouldNotBeFound("homeAddress.doesNotContain=" + DEFAULT_HOME_ADDRESS);

        // Get all the studentsList where homeAddress does not contain UPDATED_HOME_ADDRESS
        defaultStudentsShouldBeFound("homeAddress.doesNotContain=" + UPDATED_HOME_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudentsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where longitude equals to DEFAULT_LONGITUDE
        defaultStudentsShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the studentsList where longitude equals to UPDATED_LONGITUDE
        defaultStudentsShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultStudentsShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the studentsList where longitude equals to UPDATED_LONGITUDE
        defaultStudentsShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where longitude is not null
        defaultStudentsShouldBeFound("longitude.specified=true");

        // Get all the studentsList where longitude is null
        defaultStudentsShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where longitude contains DEFAULT_LONGITUDE
        defaultStudentsShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the studentsList where longitude contains UPDATED_LONGITUDE
        defaultStudentsShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where longitude does not contain DEFAULT_LONGITUDE
        defaultStudentsShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the studentsList where longitude does not contain UPDATED_LONGITUDE
        defaultStudentsShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where latitude equals to DEFAULT_LATITUDE
        defaultStudentsShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the studentsList where latitude equals to UPDATED_LATITUDE
        defaultStudentsShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultStudentsShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the studentsList where latitude equals to UPDATED_LATITUDE
        defaultStudentsShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where latitude is not null
        defaultStudentsShouldBeFound("latitude.specified=true");

        // Get all the studentsList where latitude is null
        defaultStudentsShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where latitude contains DEFAULT_LATITUDE
        defaultStudentsShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the studentsList where latitude contains UPDATED_LATITUDE
        defaultStudentsShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where latitude does not contain DEFAULT_LATITUDE
        defaultStudentsShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the studentsList where latitude does not contain UPDATED_LATITUDE
        defaultStudentsShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudentsByBillingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where billingStatus equals to DEFAULT_BILLING_STATUS
        defaultStudentsShouldBeFound("billingStatus.equals=" + DEFAULT_BILLING_STATUS);

        // Get all the studentsList where billingStatus equals to UPDATED_BILLING_STATUS
        defaultStudentsShouldNotBeFound("billingStatus.equals=" + UPDATED_BILLING_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentsByBillingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where billingStatus in DEFAULT_BILLING_STATUS or UPDATED_BILLING_STATUS
        defaultStudentsShouldBeFound("billingStatus.in=" + DEFAULT_BILLING_STATUS + "," + UPDATED_BILLING_STATUS);

        // Get all the studentsList where billingStatus equals to UPDATED_BILLING_STATUS
        defaultStudentsShouldNotBeFound("billingStatus.in=" + UPDATED_BILLING_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentsByBillingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where billingStatus is not null
        defaultStudentsShouldBeFound("billingStatus.specified=true");

        // Get all the studentsList where billingStatus is null
        defaultStudentsShouldNotBeFound("billingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByNextBillingCycleIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where nextBillingCycle equals to DEFAULT_NEXT_BILLING_CYCLE
        defaultStudentsShouldBeFound("nextBillingCycle.equals=" + DEFAULT_NEXT_BILLING_CYCLE);

        // Get all the studentsList where nextBillingCycle equals to UPDATED_NEXT_BILLING_CYCLE
        defaultStudentsShouldNotBeFound("nextBillingCycle.equals=" + UPDATED_NEXT_BILLING_CYCLE);
    }

    @Test
    @Transactional
    void getAllStudentsByNextBillingCycleIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where nextBillingCycle in DEFAULT_NEXT_BILLING_CYCLE or UPDATED_NEXT_BILLING_CYCLE
        defaultStudentsShouldBeFound("nextBillingCycle.in=" + DEFAULT_NEXT_BILLING_CYCLE + "," + UPDATED_NEXT_BILLING_CYCLE);

        // Get all the studentsList where nextBillingCycle equals to UPDATED_NEXT_BILLING_CYCLE
        defaultStudentsShouldNotBeFound("nextBillingCycle.in=" + UPDATED_NEXT_BILLING_CYCLE);
    }

    @Test
    @Transactional
    void getAllStudentsByNextBillingCycleIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where nextBillingCycle is not null
        defaultStudentsShouldBeFound("nextBillingCycle.specified=true");

        // Get all the studentsList where nextBillingCycle is null
        defaultStudentsShouldNotBeFound("nextBillingCycle.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultStudentsShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the studentsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultStudentsShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultStudentsShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the studentsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultStudentsShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where entityStatus is not null
        defaultStudentsShouldBeFound("entityStatus.specified=true");

        // Get all the studentsList where entityStatus is null
        defaultStudentsShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where creationDate equals to DEFAULT_CREATION_DATE
        defaultStudentsShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the studentsList where creationDate equals to UPDATED_CREATION_DATE
        defaultStudentsShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultStudentsShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the studentsList where creationDate equals to UPDATED_CREATION_DATE
        defaultStudentsShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where creationDate is not null
        defaultStudentsShouldBeFound("creationDate.specified=true");

        // Get all the studentsList where creationDate is null
        defaultStudentsShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultStudentsShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the studentsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultStudentsShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultStudentsShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the studentsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultStudentsShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        // Get all the studentsList where modifiedDate is not null
        defaultStudentsShouldBeFound("modifiedDate.specified=true");

        // Get all the studentsList where modifiedDate is null
        defaultStudentsShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByStudentTripsIsEqualToSomething() throws Exception {
        StudentTrips studentTrips;
        if (TestUtil.findAll(em, StudentTrips.class).isEmpty()) {
            studentsRepository.saveAndFlush(students);
            studentTrips = StudentTripsResourceIT.createEntity(em);
        } else {
            studentTrips = TestUtil.findAll(em, StudentTrips.class).get(0);
        }
        em.persist(studentTrips);
        em.flush();
        students.addStudentTrips(studentTrips);
        studentsRepository.saveAndFlush(students);
        Long studentTripsId = studentTrips.getId();
        // Get all the studentsList where studentTrips equals to studentTripsId
        defaultStudentsShouldBeFound("studentTripsId.equals=" + studentTripsId);

        // Get all the studentsList where studentTrips equals to (studentTripsId + 1)
        defaultStudentsShouldNotBeFound("studentTripsId.equals=" + (studentTripsId + 1));
    }

    @Test
    @Transactional
    void getAllStudentsByStudentBillingsIsEqualToSomething() throws Exception {
        StudentBillings studentBillings;
        if (TestUtil.findAll(em, StudentBillings.class).isEmpty()) {
            studentsRepository.saveAndFlush(students);
            studentBillings = StudentBillingsResourceIT.createEntity(em);
        } else {
            studentBillings = TestUtil.findAll(em, StudentBillings.class).get(0);
        }
        em.persist(studentBillings);
        em.flush();
        students.addStudentBillings(studentBillings);
        studentsRepository.saveAndFlush(students);
        Long studentBillingsId = studentBillings.getId();
        // Get all the studentsList where studentBillings equals to studentBillingsId
        defaultStudentsShouldBeFound("studentBillingsId.equals=" + studentBillingsId);

        // Get all the studentsList where studentBillings equals to (studentBillingsId + 1)
        defaultStudentsShouldNotBeFound("studentBillingsId.equals=" + (studentBillingsId + 1));
    }

    @Test
    @Transactional
    void getAllStudentsByFleetIsEqualToSomething() throws Exception {
        Fleet fleet;
        if (TestUtil.findAll(em, Fleet.class).isEmpty()) {
            studentsRepository.saveAndFlush(students);
            fleet = FleetResourceIT.createEntity(em);
        } else {
            fleet = TestUtil.findAll(em, Fleet.class).get(0);
        }
        em.persist(fleet);
        em.flush();
        students.setFleet(fleet);
        studentsRepository.saveAndFlush(students);
        Long fleetId = fleet.getId();
        // Get all the studentsList where fleet equals to fleetId
        defaultStudentsShouldBeFound("fleetId.equals=" + fleetId);

        // Get all the studentsList where fleet equals to (fleetId + 1)
        defaultStudentsShouldNotBeFound("fleetId.equals=" + (fleetId + 1));
    }

    @Test
    @Transactional
    void getAllStudentsByGuardianIsEqualToSomething() throws Exception {
        Guardians guardian;
        if (TestUtil.findAll(em, Guardians.class).isEmpty()) {
            studentsRepository.saveAndFlush(students);
            guardian = GuardiansResourceIT.createEntity(em);
        } else {
            guardian = TestUtil.findAll(em, Guardians.class).get(0);
        }
        em.persist(guardian);
        em.flush();
        students.setGuardian(guardian);
        studentsRepository.saveAndFlush(students);
        Long guardianId = guardian.getId();
        // Get all the studentsList where guardian equals to guardianId
        defaultStudentsShouldBeFound("guardianId.equals=" + guardianId);

        // Get all the studentsList where guardian equals to (guardianId + 1)
        defaultStudentsShouldNotBeFound("guardianId.equals=" + (guardianId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentsShouldBeFound(String filter) throws Exception {
        restStudentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(students.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].classLevel").value(hasItem(DEFAULT_CLASS_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].homeAddress").value(hasItem(DEFAULT_HOME_ADDRESS)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].billingStatus").value(hasItem(DEFAULT_BILLING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nextBillingCycle").value(hasItem(DEFAULT_NEXT_BILLING_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentsShouldNotBeFound(String filter) throws Exception {
        restStudentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudents() throws Exception {
        // Get the students
        restStudentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudents() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();

        // Update the students
        Students updatedStudents = studentsRepository.findById(students.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudents are not directly saved in db
        em.detach(updatedStudents);
        updatedStudents
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .classLevel(UPDATED_CLASS_LEVEL)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .homeAddress(UPDATED_HOME_ADDRESS)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .billingStatus(UPDATED_BILLING_STATUS)
            .nextBillingCycle(UPDATED_NEXT_BILLING_CYCLE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        StudentsDTO studentsDTO = studentsMapper.toDto(updatedStudents);

        restStudentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudents.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testStudents.getClassLevel()).isEqualTo(UPDATED_CLASS_LEVEL);
        assertThat(testStudents.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testStudents.getHomeAddress()).isEqualTo(UPDATED_HOME_ADDRESS);
        assertThat(testStudents.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testStudents.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testStudents.getBillingStatus()).isEqualTo(UPDATED_BILLING_STATUS);
        assertThat(testStudents.getNextBillingCycle()).isEqualTo(UPDATED_NEXT_BILLING_CYCLE);
        assertThat(testStudents.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testStudents.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStudents.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();
        students.setId(count.incrementAndGet());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();
        students.setId(count.incrementAndGet());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();
        students.setId(count.incrementAndGet());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentsWithPatch() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();

        // Update the students using partial update
        Students partialUpdatedStudents = new Students();
        partialUpdatedStudents.setId(students.getId());

        partialUpdatedStudents
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .classLevel(UPDATED_CLASS_LEVEL)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restStudentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudents))
            )
            .andExpect(status().isOk());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudents.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testStudents.getClassLevel()).isEqualTo(UPDATED_CLASS_LEVEL);
        assertThat(testStudents.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testStudents.getHomeAddress()).isEqualTo(DEFAULT_HOME_ADDRESS);
        assertThat(testStudents.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testStudents.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testStudents.getBillingStatus()).isEqualTo(DEFAULT_BILLING_STATUS);
        assertThat(testStudents.getNextBillingCycle()).isEqualTo(DEFAULT_NEXT_BILLING_CYCLE);
        assertThat(testStudents.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testStudents.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStudents.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentsWithPatch() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();

        // Update the students using partial update
        Students partialUpdatedStudents = new Students();
        partialUpdatedStudents.setId(students.getId());

        partialUpdatedStudents
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .classLevel(UPDATED_CLASS_LEVEL)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .homeAddress(UPDATED_HOME_ADDRESS)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .billingStatus(UPDATED_BILLING_STATUS)
            .nextBillingCycle(UPDATED_NEXT_BILLING_CYCLE)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restStudentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudents))
            )
            .andExpect(status().isOk());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudents.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testStudents.getClassLevel()).isEqualTo(UPDATED_CLASS_LEVEL);
        assertThat(testStudents.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testStudents.getHomeAddress()).isEqualTo(UPDATED_HOME_ADDRESS);
        assertThat(testStudents.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testStudents.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testStudents.getBillingStatus()).isEqualTo(UPDATED_BILLING_STATUS);
        assertThat(testStudents.getNextBillingCycle()).isEqualTo(UPDATED_NEXT_BILLING_CYCLE);
        assertThat(testStudents.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testStudents.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStudents.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();
        students.setId(count.incrementAndGet());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();
        students.setId(count.incrementAndGet());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().size();
        students.setId(count.incrementAndGet());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudents() throws Exception {
        // Initialize the database
        studentsRepository.saveAndFlush(students);

        int databaseSizeBeforeDelete = studentsRepository.findAll().size();

        // Delete the students
        restStudentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, students.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Students> studentsList = studentsRepository.findAll();
        assertThat(studentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
