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
import tech.bytespot.harmony.domain.Terminal;
import tech.bytespot.harmony.domain.enumeration.DeviceStatus;
import tech.bytespot.harmony.domain.enumeration.EntityStatus;
import tech.bytespot.harmony.repository.TerminalRepository;
import tech.bytespot.harmony.service.dto.TerminalDTO;
import tech.bytespot.harmony.service.mapper.TerminalMapper;

/**
 * Integration tests for the {@link TerminalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TerminalResourceIT {

    private static final String DEFAULT_DEVIDE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVIDE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_PING = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_PING = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final DeviceStatus DEFAULT_STATUS = DeviceStatus.ONLINE;
    private static final DeviceStatus UPDATED_STATUS = DeviceStatus.OFFLINE;

    private static final EntityStatus DEFAULT_ENTITY_STATUS = EntityStatus.ACTIVE;
    private static final EntityStatus UPDATED_ENTITY_STATUS = EntityStatus.INACTIVE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/terminals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerminalMockMvc;

    private Terminal terminal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .devideId(DEFAULT_DEVIDE_ID)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .manufacturer(DEFAULT_MANUFACTURER)
            .model(DEFAULT_MODEL)
            .lastPing(DEFAULT_LAST_PING)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .status(DEFAULT_STATUS)
            .entityStatus(DEFAULT_ENTITY_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return terminal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createUpdatedEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .devideId(UPDATED_DEVIDE_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .lastPing(UPDATED_LAST_PING)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .status(UPDATED_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return terminal;
    }

    @BeforeEach
    public void initTest() {
        terminal = createEntity(em);
    }

    @Test
    @Transactional
    void createTerminal() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();
        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);
        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isCreated());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate + 1);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDevideId()).isEqualTo(DEFAULT_DEVIDE_ID);
        assertThat(testTerminal.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTerminal.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testTerminal.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testTerminal.getLastPing()).isEqualTo(DEFAULT_LAST_PING);
        assertThat(testTerminal.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testTerminal.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testTerminal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTerminal.getEntityStatus()).isEqualTo(DEFAULT_ENTITY_STATUS);
        assertThat(testTerminal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTerminal.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTerminalWithExistingId() throws Exception {
        // Create the Terminal with an existing ID
        terminal.setId(1L);
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDevideIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setDevideId(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManufacturerIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setManufacturer(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setModel(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTerminals() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].devideId").value(hasItem(DEFAULT_DEVIDE_ID)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].lastPing").value(hasItem(DEFAULT_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get the terminal
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL_ID, terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terminal.getId().intValue()))
            .andExpect(jsonPath("$.devideId").value(DEFAULT_DEVIDE_ID))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.lastPing").value(DEFAULT_LAST_PING.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.entityStatus").value(DEFAULT_ENTITY_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTerminalsByIdFiltering() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        Long id = terminal.getId();

        defaultTerminalShouldBeFound("id.equals=" + id);
        defaultTerminalShouldNotBeFound("id.notEquals=" + id);

        defaultTerminalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTerminalShouldNotBeFound("id.greaterThan=" + id);

        defaultTerminalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTerminalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTerminalsByDevideIdIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where devideId equals to DEFAULT_DEVIDE_ID
        defaultTerminalShouldBeFound("devideId.equals=" + DEFAULT_DEVIDE_ID);

        // Get all the terminalList where devideId equals to UPDATED_DEVIDE_ID
        defaultTerminalShouldNotBeFound("devideId.equals=" + UPDATED_DEVIDE_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsByDevideIdIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where devideId in DEFAULT_DEVIDE_ID or UPDATED_DEVIDE_ID
        defaultTerminalShouldBeFound("devideId.in=" + DEFAULT_DEVIDE_ID + "," + UPDATED_DEVIDE_ID);

        // Get all the terminalList where devideId equals to UPDATED_DEVIDE_ID
        defaultTerminalShouldNotBeFound("devideId.in=" + UPDATED_DEVIDE_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsByDevideIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where devideId is not null
        defaultTerminalShouldBeFound("devideId.specified=true");

        // Get all the terminalList where devideId is null
        defaultTerminalShouldNotBeFound("devideId.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByDevideIdContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where devideId contains DEFAULT_DEVIDE_ID
        defaultTerminalShouldBeFound("devideId.contains=" + DEFAULT_DEVIDE_ID);

        // Get all the terminalList where devideId contains UPDATED_DEVIDE_ID
        defaultTerminalShouldNotBeFound("devideId.contains=" + UPDATED_DEVIDE_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsByDevideIdNotContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where devideId does not contain DEFAULT_DEVIDE_ID
        defaultTerminalShouldNotBeFound("devideId.doesNotContain=" + DEFAULT_DEVIDE_ID);

        // Get all the terminalList where devideId does not contain UPDATED_DEVIDE_ID
        defaultTerminalShouldBeFound("devideId.doesNotContain=" + UPDATED_DEVIDE_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultTerminalShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the terminalList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultTerminalShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTerminalsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultTerminalShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the terminalList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultTerminalShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTerminalsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where phoneNumber is not null
        defaultTerminalShouldBeFound("phoneNumber.specified=true");

        // Get all the terminalList where phoneNumber is null
        defaultTerminalShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultTerminalShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the terminalList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultTerminalShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTerminalsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultTerminalShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the terminalList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultTerminalShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTerminalsByManufacturerIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where manufacturer equals to DEFAULT_MANUFACTURER
        defaultTerminalShouldBeFound("manufacturer.equals=" + DEFAULT_MANUFACTURER);

        // Get all the terminalList where manufacturer equals to UPDATED_MANUFACTURER
        defaultTerminalShouldNotBeFound("manufacturer.equals=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllTerminalsByManufacturerIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where manufacturer in DEFAULT_MANUFACTURER or UPDATED_MANUFACTURER
        defaultTerminalShouldBeFound("manufacturer.in=" + DEFAULT_MANUFACTURER + "," + UPDATED_MANUFACTURER);

        // Get all the terminalList where manufacturer equals to UPDATED_MANUFACTURER
        defaultTerminalShouldNotBeFound("manufacturer.in=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllTerminalsByManufacturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where manufacturer is not null
        defaultTerminalShouldBeFound("manufacturer.specified=true");

        // Get all the terminalList where manufacturer is null
        defaultTerminalShouldNotBeFound("manufacturer.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByManufacturerContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where manufacturer contains DEFAULT_MANUFACTURER
        defaultTerminalShouldBeFound("manufacturer.contains=" + DEFAULT_MANUFACTURER);

        // Get all the terminalList where manufacturer contains UPDATED_MANUFACTURER
        defaultTerminalShouldNotBeFound("manufacturer.contains=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllTerminalsByManufacturerNotContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where manufacturer does not contain DEFAULT_MANUFACTURER
        defaultTerminalShouldNotBeFound("manufacturer.doesNotContain=" + DEFAULT_MANUFACTURER);

        // Get all the terminalList where manufacturer does not contain UPDATED_MANUFACTURER
        defaultTerminalShouldBeFound("manufacturer.doesNotContain=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllTerminalsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where model equals to DEFAULT_MODEL
        defaultTerminalShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the terminalList where model equals to UPDATED_MODEL
        defaultTerminalShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllTerminalsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultTerminalShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the terminalList where model equals to UPDATED_MODEL
        defaultTerminalShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllTerminalsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where model is not null
        defaultTerminalShouldBeFound("model.specified=true");

        // Get all the terminalList where model is null
        defaultTerminalShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByModelContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where model contains DEFAULT_MODEL
        defaultTerminalShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the terminalList where model contains UPDATED_MODEL
        defaultTerminalShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllTerminalsByModelNotContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where model does not contain DEFAULT_MODEL
        defaultTerminalShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the terminalList where model does not contain UPDATED_MODEL
        defaultTerminalShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllTerminalsByLastPingIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where lastPing equals to DEFAULT_LAST_PING
        defaultTerminalShouldBeFound("lastPing.equals=" + DEFAULT_LAST_PING);

        // Get all the terminalList where lastPing equals to UPDATED_LAST_PING
        defaultTerminalShouldNotBeFound("lastPing.equals=" + UPDATED_LAST_PING);
    }

    @Test
    @Transactional
    void getAllTerminalsByLastPingIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where lastPing in DEFAULT_LAST_PING or UPDATED_LAST_PING
        defaultTerminalShouldBeFound("lastPing.in=" + DEFAULT_LAST_PING + "," + UPDATED_LAST_PING);

        // Get all the terminalList where lastPing equals to UPDATED_LAST_PING
        defaultTerminalShouldNotBeFound("lastPing.in=" + UPDATED_LAST_PING);
    }

    @Test
    @Transactional
    void getAllTerminalsByLastPingIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where lastPing is not null
        defaultTerminalShouldBeFound("lastPing.specified=true");

        // Get all the terminalList where lastPing is null
        defaultTerminalShouldNotBeFound("lastPing.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where longitude equals to DEFAULT_LONGITUDE
        defaultTerminalShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the terminalList where longitude equals to UPDATED_LONGITUDE
        defaultTerminalShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultTerminalShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the terminalList where longitude equals to UPDATED_LONGITUDE
        defaultTerminalShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where longitude is not null
        defaultTerminalShouldBeFound("longitude.specified=true");

        // Get all the terminalList where longitude is null
        defaultTerminalShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where longitude contains DEFAULT_LONGITUDE
        defaultTerminalShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the terminalList where longitude contains UPDATED_LONGITUDE
        defaultTerminalShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where longitude does not contain DEFAULT_LONGITUDE
        defaultTerminalShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the terminalList where longitude does not contain UPDATED_LONGITUDE
        defaultTerminalShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where latitude equals to DEFAULT_LATITUDE
        defaultTerminalShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the terminalList where latitude equals to UPDATED_LATITUDE
        defaultTerminalShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultTerminalShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the terminalList where latitude equals to UPDATED_LATITUDE
        defaultTerminalShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where latitude is not null
        defaultTerminalShouldBeFound("latitude.specified=true");

        // Get all the terminalList where latitude is null
        defaultTerminalShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where latitude contains DEFAULT_LATITUDE
        defaultTerminalShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the terminalList where latitude contains UPDATED_LATITUDE
        defaultTerminalShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where latitude does not contain DEFAULT_LATITUDE
        defaultTerminalShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the terminalList where latitude does not contain UPDATED_LATITUDE
        defaultTerminalShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where status equals to DEFAULT_STATUS
        defaultTerminalShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the terminalList where status equals to UPDATED_STATUS
        defaultTerminalShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTerminalsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTerminalShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the terminalList where status equals to UPDATED_STATUS
        defaultTerminalShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTerminalsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where status is not null
        defaultTerminalShouldBeFound("status.specified=true");

        // Get all the terminalList where status is null
        defaultTerminalShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByEntityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where entityStatus equals to DEFAULT_ENTITY_STATUS
        defaultTerminalShouldBeFound("entityStatus.equals=" + DEFAULT_ENTITY_STATUS);

        // Get all the terminalList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultTerminalShouldNotBeFound("entityStatus.equals=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllTerminalsByEntityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where entityStatus in DEFAULT_ENTITY_STATUS or UPDATED_ENTITY_STATUS
        defaultTerminalShouldBeFound("entityStatus.in=" + DEFAULT_ENTITY_STATUS + "," + UPDATED_ENTITY_STATUS);

        // Get all the terminalList where entityStatus equals to UPDATED_ENTITY_STATUS
        defaultTerminalShouldNotBeFound("entityStatus.in=" + UPDATED_ENTITY_STATUS);
    }

    @Test
    @Transactional
    void getAllTerminalsByEntityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where entityStatus is not null
        defaultTerminalShouldBeFound("entityStatus.specified=true");

        // Get all the terminalList where entityStatus is null
        defaultTerminalShouldNotBeFound("entityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where creationDate equals to DEFAULT_CREATION_DATE
        defaultTerminalShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the terminalList where creationDate equals to UPDATED_CREATION_DATE
        defaultTerminalShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultTerminalShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the terminalList where creationDate equals to UPDATED_CREATION_DATE
        defaultTerminalShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where creationDate is not null
        defaultTerminalShouldBeFound("creationDate.specified=true");

        // Get all the terminalList where creationDate is null
        defaultTerminalShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultTerminalShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the terminalList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultTerminalShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultTerminalShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the terminalList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultTerminalShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList where modifiedDate is not null
        defaultTerminalShouldBeFound("modifiedDate.specified=true");

        // Get all the terminalList where modifiedDate is null
        defaultTerminalShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsByFleetIsEqualToSomething() throws Exception {
        Fleet fleet;
        if (TestUtil.findAll(em, Fleet.class).isEmpty()) {
            terminalRepository.saveAndFlush(terminal);
            fleet = FleetResourceIT.createEntity(em);
        } else {
            fleet = TestUtil.findAll(em, Fleet.class).get(0);
        }
        em.persist(fleet);
        em.flush();
        terminal.setFleet(fleet);
        fleet.setTerminal(terminal);
        terminalRepository.saveAndFlush(terminal);
        Long fleetId = fleet.getId();
        // Get all the terminalList where fleet equals to fleetId
        defaultTerminalShouldBeFound("fleetId.equals=" + fleetId);

        // Get all the terminalList where fleet equals to (fleetId + 1)
        defaultTerminalShouldNotBeFound("fleetId.equals=" + (fleetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTerminalShouldBeFound(String filter) throws Exception {
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].devideId").value(hasItem(DEFAULT_DEVIDE_ID)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].lastPing").value(hasItem(DEFAULT_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].entityStatus").value(hasItem(DEFAULT_ENTITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTerminalShouldNotBeFound(String filter) throws Exception {
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTerminal() throws Exception {
        // Get the terminal
        restTerminalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal
        Terminal updatedTerminal = terminalRepository.findById(terminal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTerminal are not directly saved in db
        em.detach(updatedTerminal);
        updatedTerminal
            .devideId(UPDATED_DEVIDE_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .lastPing(UPDATED_LAST_PING)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .status(UPDATED_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        TerminalDTO terminalDTO = terminalMapper.toDto(updatedTerminal);

        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDevideId()).isEqualTo(UPDATED_DEVIDE_ID);
        assertThat(testTerminal.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTerminal.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testTerminal.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testTerminal.getLastPing()).isEqualTo(UPDATED_LAST_PING);
        assertThat(testTerminal.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testTerminal.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testTerminal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTerminal.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testTerminal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTerminal.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTerminalWithPatch() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal using partial update
        Terminal partialUpdatedTerminal = new Terminal();
        partialUpdatedTerminal.setId(terminal.getId());

        partialUpdatedTerminal
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .lastPing(UPDATED_LAST_PING)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .status(UPDATED_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminal))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDevideId()).isEqualTo(DEFAULT_DEVIDE_ID);
        assertThat(testTerminal.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTerminal.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testTerminal.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testTerminal.getLastPing()).isEqualTo(UPDATED_LAST_PING);
        assertThat(testTerminal.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testTerminal.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testTerminal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTerminal.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testTerminal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTerminal.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTerminalWithPatch() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal using partial update
        Terminal partialUpdatedTerminal = new Terminal();
        partialUpdatedTerminal.setId(terminal.getId());

        partialUpdatedTerminal
            .devideId(UPDATED_DEVIDE_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .lastPing(UPDATED_LAST_PING)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .status(UPDATED_STATUS)
            .entityStatus(UPDATED_ENTITY_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminal))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDevideId()).isEqualTo(UPDATED_DEVIDE_ID);
        assertThat(testTerminal.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTerminal.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testTerminal.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testTerminal.getLastPing()).isEqualTo(UPDATED_LAST_PING);
        assertThat(testTerminal.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testTerminal.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testTerminal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTerminal.getEntityStatus()).isEqualTo(UPDATED_ENTITY_STATUS);
        assertThat(testTerminal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTerminal.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeDelete = terminalRepository.findAll().size();

        // Delete the terminal
        restTerminalMockMvc
            .perform(delete(ENTITY_API_URL_ID, terminal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
