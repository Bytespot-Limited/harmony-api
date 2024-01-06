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
import tech.bytespot.harmony.domain.Fleet;
import tech.bytespot.harmony.domain.Schools;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.repository.SchoolsRepository;
import tech.bytespot.harmony.service.dto.SchoolsDTO;
import tech.bytespot.harmony.service.mapper.SchoolsMapper;

/**
 * Integration tests for the {@link SchoolsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchoolsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_IMAGE_URL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/schools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolsRepository schoolsRepository;

    @Autowired
    private SchoolsMapper schoolsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolsMockMvc;

    private Schools schools;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schools createEntity(EntityManager em) {
        Schools schools = new Schools()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .logoImageUrl(DEFAULT_LOGO_IMAGE_URL)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return schools;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schools createUpdatedEntity(EntityManager em) {
        Schools schools = new Schools()
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .logoImageUrl(UPDATED_LOGO_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return schools;
    }

    @BeforeEach
    public void initTest() {
        schools = createEntity(em);
    }

    @Test
    @Transactional
    void createSchools() throws Exception {
        int databaseSizeBeforeCreate = schoolsRepository.findAll().size();
        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);
        restSchoolsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isCreated());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeCreate + 1);
        Schools testSchools = schoolsList.get(schoolsList.size() - 1);
        assertThat(testSchools.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSchools.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSchools.getLogoImageUrl()).isEqualTo(DEFAULT_LOGO_IMAGE_URL);
        assertThat(testSchools.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testSchools.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSchools.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testSchools.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSchools.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createSchoolsWithExistingId() throws Exception {
        // Create the Schools with an existing ID
        schools.setId(1L);
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        int databaseSizeBeforeCreate = schoolsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolsRepository.findAll().size();
        // set the field null
        schools.setName(null);

        // Create the Schools, which fails.
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        restSchoolsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isBadRequest());

        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolsRepository.findAll().size();
        // set the field null
        schools.setLocation(null);

        // Create the Schools, which fails.
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        restSchoolsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isBadRequest());

        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolsRepository.findAll().size();
        // set the field null
        schools.setEmailAddress(null);

        // Create the Schools, which fails.
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        restSchoolsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isBadRequest());

        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolsRepository.findAll().size();
        // set the field null
        schools.setPhoneNumber(null);

        // Create the Schools, which fails.
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        restSchoolsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isBadRequest());

        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchools() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList
        restSchoolsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schools.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].logoImageUrl").value(hasItem(DEFAULT_LOGO_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSchools() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get the schools
        restSchoolsMockMvc
            .perform(get(ENTITY_API_URL_ID, schools.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schools.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.logoImageUrl").value(DEFAULT_LOGO_IMAGE_URL))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolsByIdFiltering() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        Long id = schools.getId();

        defaultSchoolsShouldBeFound("id.equals=" + id);
        defaultSchoolsShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolsShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where name equals to DEFAULT_NAME
        defaultSchoolsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the schoolsList where name equals to UPDATED_NAME
        defaultSchoolsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSchoolsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the schoolsList where name equals to UPDATED_NAME
        defaultSchoolsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where name is not null
        defaultSchoolsShouldBeFound("name.specified=true");

        // Get all the schoolsList where name is null
        defaultSchoolsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByNameContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where name contains DEFAULT_NAME
        defaultSchoolsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the schoolsList where name contains UPDATED_NAME
        defaultSchoolsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where name does not contain DEFAULT_NAME
        defaultSchoolsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the schoolsList where name does not contain UPDATED_NAME
        defaultSchoolsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where location equals to DEFAULT_LOCATION
        defaultSchoolsShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the schoolsList where location equals to UPDATED_LOCATION
        defaultSchoolsShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchoolsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultSchoolsShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the schoolsList where location equals to UPDATED_LOCATION
        defaultSchoolsShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchoolsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where location is not null
        defaultSchoolsShouldBeFound("location.specified=true");

        // Get all the schoolsList where location is null
        defaultSchoolsShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByLocationContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where location contains DEFAULT_LOCATION
        defaultSchoolsShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the schoolsList where location contains UPDATED_LOCATION
        defaultSchoolsShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchoolsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where location does not contain DEFAULT_LOCATION
        defaultSchoolsShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the schoolsList where location does not contain UPDATED_LOCATION
        defaultSchoolsShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchoolsByLogoImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where logoImageUrl equals to DEFAULT_LOGO_IMAGE_URL
        defaultSchoolsShouldBeFound("logoImageUrl.equals=" + DEFAULT_LOGO_IMAGE_URL);

        // Get all the schoolsList where logoImageUrl equals to UPDATED_LOGO_IMAGE_URL
        defaultSchoolsShouldNotBeFound("logoImageUrl.equals=" + UPDATED_LOGO_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolsByLogoImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where logoImageUrl in DEFAULT_LOGO_IMAGE_URL or UPDATED_LOGO_IMAGE_URL
        defaultSchoolsShouldBeFound("logoImageUrl.in=" + DEFAULT_LOGO_IMAGE_URL + "," + UPDATED_LOGO_IMAGE_URL);

        // Get all the schoolsList where logoImageUrl equals to UPDATED_LOGO_IMAGE_URL
        defaultSchoolsShouldNotBeFound("logoImageUrl.in=" + UPDATED_LOGO_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolsByLogoImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where logoImageUrl is not null
        defaultSchoolsShouldBeFound("logoImageUrl.specified=true");

        // Get all the schoolsList where logoImageUrl is null
        defaultSchoolsShouldNotBeFound("logoImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByLogoImageUrlContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where logoImageUrl contains DEFAULT_LOGO_IMAGE_URL
        defaultSchoolsShouldBeFound("logoImageUrl.contains=" + DEFAULT_LOGO_IMAGE_URL);

        // Get all the schoolsList where logoImageUrl contains UPDATED_LOGO_IMAGE_URL
        defaultSchoolsShouldNotBeFound("logoImageUrl.contains=" + UPDATED_LOGO_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolsByLogoImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where logoImageUrl does not contain DEFAULT_LOGO_IMAGE_URL
        defaultSchoolsShouldNotBeFound("logoImageUrl.doesNotContain=" + DEFAULT_LOGO_IMAGE_URL);

        // Get all the schoolsList where logoImageUrl does not contain UPDATED_LOGO_IMAGE_URL
        defaultSchoolsShouldBeFound("logoImageUrl.doesNotContain=" + UPDATED_LOGO_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllSchoolsByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultSchoolsShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the schoolsList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultSchoolsShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultSchoolsShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the schoolsList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultSchoolsShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where emailAddress is not null
        defaultSchoolsShouldBeFound("emailAddress.specified=true");

        // Get all the schoolsList where emailAddress is null
        defaultSchoolsShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultSchoolsShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the schoolsList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultSchoolsShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultSchoolsShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the schoolsList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultSchoolsShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultSchoolsShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the schoolsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSchoolsShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultSchoolsShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the schoolsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSchoolsShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where phoneNumber is not null
        defaultSchoolsShouldBeFound("phoneNumber.specified=true");

        // Get all the schoolsList where phoneNumber is null
        defaultSchoolsShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultSchoolsShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the schoolsList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultSchoolsShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultSchoolsShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the schoolsList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultSchoolsShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultSchoolsShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the schoolsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultSchoolsShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllSchoolsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultSchoolsShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the schoolsList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultSchoolsShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllSchoolsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where entityStatus is not null
        defaultSchoolsShouldBeFound("entityStatus.specified=true");

        // Get all the schoolsList where entityStatus is null
        defaultSchoolsShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where creationDate equals to DEFAULT_CREATION_DATE
        defaultSchoolsShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the schoolsList where creationDate equals to UPDATED_CREATION_DATE
        defaultSchoolsShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultSchoolsShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the schoolsList where creationDate equals to UPDATED_CREATION_DATE
        defaultSchoolsShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where creationDate is not null
        defaultSchoolsShouldBeFound("creationDate.specified=true");

        // Get all the schoolsList where creationDate is null
        defaultSchoolsShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultSchoolsShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the schoolsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultSchoolsShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultSchoolsShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the schoolsList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultSchoolsShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        // Get all the schoolsList where modifiedDate is not null
        defaultSchoolsShouldBeFound("modifiedDate.specified=true");

        // Get all the schoolsList where modifiedDate is null
        defaultSchoolsShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByFleetIsEqualToSomething() throws Exception {
        Fleet fleet;
        if (TestUtil.findAll(em, Fleet.class).isEmpty()) {
            schoolsRepository.saveAndFlush(schools);
            fleet = FleetResourceIT.createEntity(em);
        } else {
            fleet = TestUtil.findAll(em, Fleet.class).get(0);
        }
        em.persist(fleet);
        em.flush();
        schools.addFleet(fleet);
        schoolsRepository.saveAndFlush(schools);
        Long fleetId = fleet.getId();
        // Get all the schoolsList where fleet equals to fleetId
        defaultSchoolsShouldBeFound("fleetId.equals=" + fleetId);

        // Get all the schoolsList where fleet equals to (fleetId + 1)
        defaultSchoolsShouldNotBeFound("fleetId.equals=" + (fleetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolsShouldBeFound(String filter) throws Exception {
        restSchoolsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schools.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].logoImageUrl").value(hasItem(DEFAULT_LOGO_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolsShouldNotBeFound(String filter) throws Exception {
        restSchoolsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchools() throws Exception {
        // Get the schools
        restSchoolsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchools() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();

        // Update the schools
        Schools updatedSchools = schoolsRepository.findById(schools.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchools are not directly saved in db
        em.detach(updatedSchools);
        updatedSchools
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .logoImageUrl(UPDATED_LOGO_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(updatedSchools);

        restSchoolsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
        Schools testSchools = schoolsList.get(schoolsList.size() - 1);
        assertThat(testSchools.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchools.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSchools.getLogoImageUrl()).isEqualTo(UPDATED_LOGO_IMAGE_URL);
        assertThat(testSchools.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSchools.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSchools.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testSchools.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSchools.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchools() throws Exception {
        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();
        schools.setId(count.incrementAndGet());

        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchools() throws Exception {
        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();
        schools.setId(count.incrementAndGet());

        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchools() throws Exception {
        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();
        schools.setId(count.incrementAndGet());

        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolsWithPatch() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();

        // Update the schools using partial update
        Schools partialUpdatedSchools = new Schools();
        partialUpdatedSchools.setId(schools.getId());

        partialUpdatedSchools
            .name(UPDATED_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .creationDate(UPDATED_CREATION_DATE);

        restSchoolsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchools.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchools))
            )
            .andExpect(status().isOk());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
        Schools testSchools = schoolsList.get(schoolsList.size() - 1);
        assertThat(testSchools.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchools.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSchools.getLogoImageUrl()).isEqualTo(DEFAULT_LOGO_IMAGE_URL);
        assertThat(testSchools.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSchools.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSchools.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testSchools.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSchools.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolsWithPatch() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();

        // Update the schools using partial update
        Schools partialUpdatedSchools = new Schools();
        partialUpdatedSchools.setId(schools.getId());

        partialUpdatedSchools
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .logoImageUrl(UPDATED_LOGO_IMAGE_URL)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSchoolsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchools.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchools))
            )
            .andExpect(status().isOk());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
        Schools testSchools = schoolsList.get(schoolsList.size() - 1);
        assertThat(testSchools.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSchools.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSchools.getLogoImageUrl()).isEqualTo(UPDATED_LOGO_IMAGE_URL);
        assertThat(testSchools.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testSchools.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSchools.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testSchools.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSchools.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchools() throws Exception {
        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();
        schools.setId(count.incrementAndGet());

        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchools() throws Exception {
        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();
        schools.setId(count.incrementAndGet());

        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchools() throws Exception {
        int databaseSizeBeforeUpdate = schoolsRepository.findAll().size();
        schools.setId(count.incrementAndGet());

        // Create the Schools
        SchoolsDTO schoolsDTO = schoolsMapper.toDto(schools);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schoolsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schools in the database
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchools() throws Exception {
        // Initialize the database
        schoolsRepository.saveAndFlush(schools);

        int databaseSizeBeforeDelete = schoolsRepository.findAll().size();

        // Delete the schools
        restSchoolsMockMvc
            .perform(delete(ENTITY_API_URL_ID, schools.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Schools> schoolsList = schoolsRepository.findAll();
        assertThat(schoolsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
