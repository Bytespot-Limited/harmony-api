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
import tech.bytespot.harmony.domain.StudentBillings;
import tech.bytespot.harmony.domain.Students;
import tech.bytespot.harmony.repository.StudentBillingsRepository;
import tech.bytespot.harmony.service.dto.StudentBillingsDTO;
import tech.bytespot.harmony.service.mapper.StudentBillingsMapper;

/**
 * Integration tests for the {@link StudentBillingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentBillingsResourceIT {

    private static final String DEFAULT_PAYMENT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUBSCRIPTION_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIPTION_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SUBSCRIPTION_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIPTION_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/student-billings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentBillingsRepository studentBillingsRepository;

    @Autowired
    private StudentBillingsMapper studentBillingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentBillingsMockMvc;

    private StudentBillings studentBillings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentBillings createEntity(EntityManager em) {
        StudentBillings studentBillings = new StudentBillings()
            .paymentChannel(DEFAULT_PAYMENT_CHANNEL)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .subscriptionStart(DEFAULT_SUBSCRIPTION_START)
            .subscriptionEnd(DEFAULT_SUBSCRIPTION_END);
        return studentBillings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentBillings createUpdatedEntity(EntityManager em) {
        StudentBillings studentBillings = new StudentBillings()
            .paymentChannel(UPDATED_PAYMENT_CHANNEL)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .subscriptionStart(UPDATED_SUBSCRIPTION_START)
            .subscriptionEnd(UPDATED_SUBSCRIPTION_END);
        return studentBillings;
    }

    @BeforeEach
    public void initTest() {
        studentBillings = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentBillings() throws Exception {
        int databaseSizeBeforeCreate = studentBillingsRepository.findAll().size();
        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);
        restStudentBillingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentBillings testStudentBillings = studentBillingsList.get(studentBillingsList.size() - 1);
        assertThat(testStudentBillings.getPaymentChannel()).isEqualTo(DEFAULT_PAYMENT_CHANNEL);
        assertThat(testStudentBillings.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testStudentBillings.getSubscriptionStart()).isEqualTo(DEFAULT_SUBSCRIPTION_START);
        assertThat(testStudentBillings.getSubscriptionEnd()).isEqualTo(DEFAULT_SUBSCRIPTION_END);
    }

    @Test
    @Transactional
    void createStudentBillingsWithExistingId() throws Exception {
        // Create the StudentBillings with an existing ID
        studentBillings.setId(1L);
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        int databaseSizeBeforeCreate = studentBillingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentBillingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPaymentChannelIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentBillingsRepository.findAll().size();
        // set the field null
        studentBillings.setPaymentChannel(null);

        // Create the StudentBillings, which fails.
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        restStudentBillingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentBillingsRepository.findAll().size();
        // set the field null
        studentBillings.setPaymentReference(null);

        // Create the StudentBillings, which fails.
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        restStudentBillingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentBillings() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList
        restStudentBillingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentBillings.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentChannel").value(hasItem(DEFAULT_PAYMENT_CHANNEL)))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].subscriptionStart").value(hasItem(DEFAULT_SUBSCRIPTION_START.toString())))
            .andExpect(jsonPath("$.[*].subscriptionEnd").value(hasItem(DEFAULT_SUBSCRIPTION_END.toString())));
    }

    @Test
    @Transactional
    void getStudentBillings() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get the studentBillings
        restStudentBillingsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentBillings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentBillings.getId().intValue()))
            .andExpect(jsonPath("$.paymentChannel").value(DEFAULT_PAYMENT_CHANNEL))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE))
            .andExpect(jsonPath("$.subscriptionStart").value(DEFAULT_SUBSCRIPTION_START.toString()))
            .andExpect(jsonPath("$.subscriptionEnd").value(DEFAULT_SUBSCRIPTION_END.toString()));
    }

    @Test
    @Transactional
    void getStudentBillingsByIdFiltering() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        Long id = studentBillings.getId();

        defaultStudentBillingsShouldBeFound("id.equals=" + id);
        defaultStudentBillingsShouldNotBeFound("id.notEquals=" + id);

        defaultStudentBillingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentBillingsShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentBillingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentBillingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentChannel equals to DEFAULT_PAYMENT_CHANNEL
        defaultStudentBillingsShouldBeFound("paymentChannel.equals=" + DEFAULT_PAYMENT_CHANNEL);

        // Get all the studentBillingsList where paymentChannel equals to UPDATED_PAYMENT_CHANNEL
        defaultStudentBillingsShouldNotBeFound("paymentChannel.equals=" + UPDATED_PAYMENT_CHANNEL);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentChannelIsInShouldWork() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentChannel in DEFAULT_PAYMENT_CHANNEL or UPDATED_PAYMENT_CHANNEL
        defaultStudentBillingsShouldBeFound("paymentChannel.in=" + DEFAULT_PAYMENT_CHANNEL + "," + UPDATED_PAYMENT_CHANNEL);

        // Get all the studentBillingsList where paymentChannel equals to UPDATED_PAYMENT_CHANNEL
        defaultStudentBillingsShouldNotBeFound("paymentChannel.in=" + UPDATED_PAYMENT_CHANNEL);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentChannel is not null
        defaultStudentBillingsShouldBeFound("paymentChannel.specified=true");

        // Get all the studentBillingsList where paymentChannel is null
        defaultStudentBillingsShouldNotBeFound("paymentChannel.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentChannelContainsSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentChannel contains DEFAULT_PAYMENT_CHANNEL
        defaultStudentBillingsShouldBeFound("paymentChannel.contains=" + DEFAULT_PAYMENT_CHANNEL);

        // Get all the studentBillingsList where paymentChannel contains UPDATED_PAYMENT_CHANNEL
        defaultStudentBillingsShouldNotBeFound("paymentChannel.contains=" + UPDATED_PAYMENT_CHANNEL);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentChannelNotContainsSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentChannel does not contain DEFAULT_PAYMENT_CHANNEL
        defaultStudentBillingsShouldNotBeFound("paymentChannel.doesNotContain=" + DEFAULT_PAYMENT_CHANNEL);

        // Get all the studentBillingsList where paymentChannel does not contain UPDATED_PAYMENT_CHANNEL
        defaultStudentBillingsShouldBeFound("paymentChannel.doesNotContain=" + UPDATED_PAYMENT_CHANNEL);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentReference equals to DEFAULT_PAYMENT_REFERENCE
        defaultStudentBillingsShouldBeFound("paymentReference.equals=" + DEFAULT_PAYMENT_REFERENCE);

        // Get all the studentBillingsList where paymentReference equals to UPDATED_PAYMENT_REFERENCE
        defaultStudentBillingsShouldNotBeFound("paymentReference.equals=" + UPDATED_PAYMENT_REFERENCE);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentReference in DEFAULT_PAYMENT_REFERENCE or UPDATED_PAYMENT_REFERENCE
        defaultStudentBillingsShouldBeFound("paymentReference.in=" + DEFAULT_PAYMENT_REFERENCE + "," + UPDATED_PAYMENT_REFERENCE);

        // Get all the studentBillingsList where paymentReference equals to UPDATED_PAYMENT_REFERENCE
        defaultStudentBillingsShouldNotBeFound("paymentReference.in=" + UPDATED_PAYMENT_REFERENCE);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentReference is not null
        defaultStudentBillingsShouldBeFound("paymentReference.specified=true");

        // Get all the studentBillingsList where paymentReference is null
        defaultStudentBillingsShouldNotBeFound("paymentReference.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentReferenceContainsSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentReference contains DEFAULT_PAYMENT_REFERENCE
        defaultStudentBillingsShouldBeFound("paymentReference.contains=" + DEFAULT_PAYMENT_REFERENCE);

        // Get all the studentBillingsList where paymentReference contains UPDATED_PAYMENT_REFERENCE
        defaultStudentBillingsShouldNotBeFound("paymentReference.contains=" + UPDATED_PAYMENT_REFERENCE);
    }

    @Test
    @Transactional
    void getAllStudentBillingsByPaymentReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where paymentReference does not contain DEFAULT_PAYMENT_REFERENCE
        defaultStudentBillingsShouldNotBeFound("paymentReference.doesNotContain=" + DEFAULT_PAYMENT_REFERENCE);

        // Get all the studentBillingsList where paymentReference does not contain UPDATED_PAYMENT_REFERENCE
        defaultStudentBillingsShouldBeFound("paymentReference.doesNotContain=" + UPDATED_PAYMENT_REFERENCE);
    }

    @Test
    @Transactional
    void getAllStudentBillingsBySubscriptionStartIsEqualToSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where subscriptionStart equals to DEFAULT_SUBSCRIPTION_START
        defaultStudentBillingsShouldBeFound("subscriptionStart.equals=" + DEFAULT_SUBSCRIPTION_START);

        // Get all the studentBillingsList where subscriptionStart equals to UPDATED_SUBSCRIPTION_START
        defaultStudentBillingsShouldNotBeFound("subscriptionStart.equals=" + UPDATED_SUBSCRIPTION_START);
    }

    @Test
    @Transactional
    void getAllStudentBillingsBySubscriptionStartIsInShouldWork() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where subscriptionStart in DEFAULT_SUBSCRIPTION_START or UPDATED_SUBSCRIPTION_START
        defaultStudentBillingsShouldBeFound("subscriptionStart.in=" + DEFAULT_SUBSCRIPTION_START + "," + UPDATED_SUBSCRIPTION_START);

        // Get all the studentBillingsList where subscriptionStart equals to UPDATED_SUBSCRIPTION_START
        defaultStudentBillingsShouldNotBeFound("subscriptionStart.in=" + UPDATED_SUBSCRIPTION_START);
    }

    @Test
    @Transactional
    void getAllStudentBillingsBySubscriptionStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where subscriptionStart is not null
        defaultStudentBillingsShouldBeFound("subscriptionStart.specified=true");

        // Get all the studentBillingsList where subscriptionStart is null
        defaultStudentBillingsShouldNotBeFound("subscriptionStart.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentBillingsBySubscriptionEndIsEqualToSomething() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where subscriptionEnd equals to DEFAULT_SUBSCRIPTION_END
        defaultStudentBillingsShouldBeFound("subscriptionEnd.equals=" + DEFAULT_SUBSCRIPTION_END);

        // Get all the studentBillingsList where subscriptionEnd equals to UPDATED_SUBSCRIPTION_END
        defaultStudentBillingsShouldNotBeFound("subscriptionEnd.equals=" + UPDATED_SUBSCRIPTION_END);
    }

    @Test
    @Transactional
    void getAllStudentBillingsBySubscriptionEndIsInShouldWork() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where subscriptionEnd in DEFAULT_SUBSCRIPTION_END or UPDATED_SUBSCRIPTION_END
        defaultStudentBillingsShouldBeFound("subscriptionEnd.in=" + DEFAULT_SUBSCRIPTION_END + "," + UPDATED_SUBSCRIPTION_END);

        // Get all the studentBillingsList where subscriptionEnd equals to UPDATED_SUBSCRIPTION_END
        defaultStudentBillingsShouldNotBeFound("subscriptionEnd.in=" + UPDATED_SUBSCRIPTION_END);
    }

    @Test
    @Transactional
    void getAllStudentBillingsBySubscriptionEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        // Get all the studentBillingsList where subscriptionEnd is not null
        defaultStudentBillingsShouldBeFound("subscriptionEnd.specified=true");

        // Get all the studentBillingsList where subscriptionEnd is null
        defaultStudentBillingsShouldNotBeFound("subscriptionEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentBillingsByStudentIsEqualToSomething() throws Exception {
        Students student;
        if (TestUtil.findAll(em, Students.class).isEmpty()) {
            studentBillingsRepository.saveAndFlush(studentBillings);
            student = StudentsResourceIT.createEntity(em);
        } else {
            student = TestUtil.findAll(em, Students.class).get(0);
        }
        em.persist(student);
        em.flush();
        studentBillings.setStudent(student);
        studentBillingsRepository.saveAndFlush(studentBillings);
        Long studentId = student.getId();
        // Get all the studentBillingsList where student equals to studentId
        defaultStudentBillingsShouldBeFound("studentId.equals=" + studentId);

        // Get all the studentBillingsList where student equals to (studentId + 1)
        defaultStudentBillingsShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentBillingsShouldBeFound(String filter) throws Exception {
        restStudentBillingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentBillings.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentChannel").value(hasItem(DEFAULT_PAYMENT_CHANNEL)))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].subscriptionStart").value(hasItem(DEFAULT_SUBSCRIPTION_START.toString())))
            .andExpect(jsonPath("$.[*].subscriptionEnd").value(hasItem(DEFAULT_SUBSCRIPTION_END.toString())));

        // Check, that the count call also returns 1
        restStudentBillingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentBillingsShouldNotBeFound(String filter) throws Exception {
        restStudentBillingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentBillingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentBillings() throws Exception {
        // Get the studentBillings
        restStudentBillingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentBillings() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();

        // Update the studentBillings
        StudentBillings updatedStudentBillings = studentBillingsRepository.findById(studentBillings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentBillings are not directly saved in db
        em.detach(updatedStudentBillings);
        updatedStudentBillings
            .paymentChannel(UPDATED_PAYMENT_CHANNEL)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .subscriptionStart(UPDATED_SUBSCRIPTION_START)
            .subscriptionEnd(UPDATED_SUBSCRIPTION_END);
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(updatedStudentBillings);

        restStudentBillingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentBillingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
        StudentBillings testStudentBillings = studentBillingsList.get(studentBillingsList.size() - 1);
        assertThat(testStudentBillings.getPaymentChannel()).isEqualTo(UPDATED_PAYMENT_CHANNEL);
        assertThat(testStudentBillings.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testStudentBillings.getSubscriptionStart()).isEqualTo(UPDATED_SUBSCRIPTION_START);
        assertThat(testStudentBillings.getSubscriptionEnd()).isEqualTo(UPDATED_SUBSCRIPTION_END);
    }

    @Test
    @Transactional
    void putNonExistingStudentBillings() throws Exception {
        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();
        studentBillings.setId(count.incrementAndGet());

        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentBillingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentBillingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentBillings() throws Exception {
        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();
        studentBillings.setId(count.incrementAndGet());

        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentBillingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentBillings() throws Exception {
        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();
        studentBillings.setId(count.incrementAndGet());

        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentBillingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentBillingsWithPatch() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();

        // Update the studentBillings using partial update
        StudentBillings partialUpdatedStudentBillings = new StudentBillings();
        partialUpdatedStudentBillings.setId(studentBillings.getId());

        partialUpdatedStudentBillings
            .paymentChannel(UPDATED_PAYMENT_CHANNEL)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .subscriptionStart(UPDATED_SUBSCRIPTION_START);

        restStudentBillingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentBillings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentBillings))
            )
            .andExpect(status().isOk());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
        StudentBillings testStudentBillings = studentBillingsList.get(studentBillingsList.size() - 1);
        assertThat(testStudentBillings.getPaymentChannel()).isEqualTo(UPDATED_PAYMENT_CHANNEL);
        assertThat(testStudentBillings.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testStudentBillings.getSubscriptionStart()).isEqualTo(UPDATED_SUBSCRIPTION_START);
        assertThat(testStudentBillings.getSubscriptionEnd()).isEqualTo(DEFAULT_SUBSCRIPTION_END);
    }

    @Test
    @Transactional
    void fullUpdateStudentBillingsWithPatch() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();

        // Update the studentBillings using partial update
        StudentBillings partialUpdatedStudentBillings = new StudentBillings();
        partialUpdatedStudentBillings.setId(studentBillings.getId());

        partialUpdatedStudentBillings
            .paymentChannel(UPDATED_PAYMENT_CHANNEL)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .subscriptionStart(UPDATED_SUBSCRIPTION_START)
            .subscriptionEnd(UPDATED_SUBSCRIPTION_END);

        restStudentBillingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentBillings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentBillings))
            )
            .andExpect(status().isOk());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
        StudentBillings testStudentBillings = studentBillingsList.get(studentBillingsList.size() - 1);
        assertThat(testStudentBillings.getPaymentChannel()).isEqualTo(UPDATED_PAYMENT_CHANNEL);
        assertThat(testStudentBillings.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testStudentBillings.getSubscriptionStart()).isEqualTo(UPDATED_SUBSCRIPTION_START);
        assertThat(testStudentBillings.getSubscriptionEnd()).isEqualTo(UPDATED_SUBSCRIPTION_END);
    }

    @Test
    @Transactional
    void patchNonExistingStudentBillings() throws Exception {
        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();
        studentBillings.setId(count.incrementAndGet());

        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentBillingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentBillingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentBillings() throws Exception {
        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();
        studentBillings.setId(count.incrementAndGet());

        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentBillingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentBillings() throws Exception {
        int databaseSizeBeforeUpdate = studentBillingsRepository.findAll().size();
        studentBillings.setId(count.incrementAndGet());

        // Create the StudentBillings
        StudentBillingsDTO studentBillingsDTO = studentBillingsMapper.toDto(studentBillings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentBillingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentBillingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentBillings in the database
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentBillings() throws Exception {
        // Initialize the database
        studentBillingsRepository.saveAndFlush(studentBillings);

        int databaseSizeBeforeDelete = studentBillingsRepository.findAll().size();

        // Delete the studentBillings
        restStudentBillingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentBillings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentBillings> studentBillingsList = studentBillingsRepository.findAll();
        assertThat(studentBillingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
