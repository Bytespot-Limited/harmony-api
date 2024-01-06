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
import tech.bytespot.harmony.domain.Notifications;
import tech.bytespot.harmony.domain.enumeration.NotificationChannel;
import tech.bytespot.harmony.domain.enumeration.NotificationStatus;
import tech.bytespot.harmony.repository.NotificationsRepository;
import tech.bytespot.harmony.service.dto.NotificationsDTO;
import tech.bytespot.harmony.service.mapper.NotificationsMapper;

/**
 * Integration tests for the {@link NotificationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationsResourceIT {

    private static final NotificationChannel DEFAULT_CHANNEL = NotificationChannel.SMS;
    private static final NotificationChannel UPDATED_CHANNEL = NotificationChannel.EMAIL;

    private static final String DEFAULT_CHANNEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final NotificationStatus DEFAULT_STATUS = NotificationStatus.SENT;
    private static final NotificationStatus UPDATED_STATUS = NotificationStatus.DELIVERED;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private NotificationsMapper notificationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationsMockMvc;

    private Notifications notifications;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notifications createEntity(EntityManager em) {
        Notifications notifications = new Notifications()
            .channel(DEFAULT_CHANNEL)
            .channelId(DEFAULT_CHANNEL_ID)
            .message(DEFAULT_MESSAGE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .status(DEFAULT_STATUS)
            .creationDate(DEFAULT_CREATION_DATE);
        return notifications;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notifications createUpdatedEntity(EntityManager em) {
        Notifications notifications = new Notifications()
            .channel(UPDATED_CHANNEL)
            .channelId(UPDATED_CHANNEL_ID)
            .message(UPDATED_MESSAGE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);
        return notifications;
    }

    @BeforeEach
    public void initTest() {
        notifications = createEntity(em);
    }

    @Test
    @Transactional
    void createNotifications() throws Exception {
        int databaseSizeBeforeCreate = notificationsRepository.findAll().size();
        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);
        restNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeCreate + 1);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testNotifications.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testNotifications.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotifications.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
        assertThat(testNotifications.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNotifications.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createNotificationsWithExistingId() throws Exception {
        // Create the Notifications with an existing ID
        notifications.setId(1L);
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        int databaseSizeBeforeCreate = notificationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationsRepository.findAll().size();
        // set the field null
        notifications.setChannel(null);

        // Create the Notifications, which fails.
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        restNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationsRepository.findAll().size();
        // set the field null
        notifications.setChannelId(null);

        // Create the Notifications, which fails.
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        restNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationsRepository.findAll().size();
        // set the field null
        notifications.setMessage(null);

        // Create the Notifications, which fails.
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        restNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get the notifications
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL_ID, notifications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notifications.getId().intValue()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        Long id = notifications.getId();

        defaultNotificationsShouldBeFound("id.equals=" + id);
        defaultNotificationsShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationsShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channel equals to DEFAULT_CHANNEL
        defaultNotificationsShouldBeFound("channel.equals=" + DEFAULT_CHANNEL);

        // Get all the notificationsList where channel equals to UPDATED_CHANNEL
        defaultNotificationsShouldNotBeFound("channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channel in DEFAULT_CHANNEL or UPDATED_CHANNEL
        defaultNotificationsShouldBeFound("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL);

        // Get all the notificationsList where channel equals to UPDATED_CHANNEL
        defaultNotificationsShouldNotBeFound("channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channel is not null
        defaultNotificationsShouldBeFound("channel.specified=true");

        // Get all the notificationsList where channel is null
        defaultNotificationsShouldNotBeFound("channel.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channelId equals to DEFAULT_CHANNEL_ID
        defaultNotificationsShouldBeFound("channelId.equals=" + DEFAULT_CHANNEL_ID);

        // Get all the notificationsList where channelId equals to UPDATED_CHANNEL_ID
        defaultNotificationsShouldNotBeFound("channelId.equals=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIdIsInShouldWork() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channelId in DEFAULT_CHANNEL_ID or UPDATED_CHANNEL_ID
        defaultNotificationsShouldBeFound("channelId.in=" + DEFAULT_CHANNEL_ID + "," + UPDATED_CHANNEL_ID);

        // Get all the notificationsList where channelId equals to UPDATED_CHANNEL_ID
        defaultNotificationsShouldNotBeFound("channelId.in=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channelId is not null
        defaultNotificationsShouldBeFound("channelId.specified=true");

        // Get all the notificationsList where channelId is null
        defaultNotificationsShouldNotBeFound("channelId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIdContainsSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channelId contains DEFAULT_CHANNEL_ID
        defaultNotificationsShouldBeFound("channelId.contains=" + DEFAULT_CHANNEL_ID);

        // Get all the notificationsList where channelId contains UPDATED_CHANNEL_ID
        defaultNotificationsShouldNotBeFound("channelId.contains=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIdNotContainsSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where channelId does not contain DEFAULT_CHANNEL_ID
        defaultNotificationsShouldNotBeFound("channelId.doesNotContain=" + DEFAULT_CHANNEL_ID);

        // Get all the notificationsList where channelId does not contain UPDATED_CHANNEL_ID
        defaultNotificationsShouldBeFound("channelId.doesNotContain=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where message equals to DEFAULT_MESSAGE
        defaultNotificationsShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the notificationsList where message equals to UPDATED_MESSAGE
        defaultNotificationsShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultNotificationsShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the notificationsList where message equals to UPDATED_MESSAGE
        defaultNotificationsShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where message is not null
        defaultNotificationsShouldBeFound("message.specified=true");

        // Get all the notificationsList where message is null
        defaultNotificationsShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageContainsSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where message contains DEFAULT_MESSAGE
        defaultNotificationsShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the notificationsList where message contains UPDATED_MESSAGE
        defaultNotificationsShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where message does not contain DEFAULT_MESSAGE
        defaultNotificationsShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the notificationsList where message does not contain UPDATED_MESSAGE
        defaultNotificationsShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByErrorMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where errorMessage equals to DEFAULT_ERROR_MESSAGE
        defaultNotificationsShouldBeFound("errorMessage.equals=" + DEFAULT_ERROR_MESSAGE);

        // Get all the notificationsList where errorMessage equals to UPDATED_ERROR_MESSAGE
        defaultNotificationsShouldNotBeFound("errorMessage.equals=" + UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByErrorMessageIsInShouldWork() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where errorMessage in DEFAULT_ERROR_MESSAGE or UPDATED_ERROR_MESSAGE
        defaultNotificationsShouldBeFound("errorMessage.in=" + DEFAULT_ERROR_MESSAGE + "," + UPDATED_ERROR_MESSAGE);

        // Get all the notificationsList where errorMessage equals to UPDATED_ERROR_MESSAGE
        defaultNotificationsShouldNotBeFound("errorMessage.in=" + UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByErrorMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where errorMessage is not null
        defaultNotificationsShouldBeFound("errorMessage.specified=true");

        // Get all the notificationsList where errorMessage is null
        defaultNotificationsShouldNotBeFound("errorMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByErrorMessageContainsSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where errorMessage contains DEFAULT_ERROR_MESSAGE
        defaultNotificationsShouldBeFound("errorMessage.contains=" + DEFAULT_ERROR_MESSAGE);

        // Get all the notificationsList where errorMessage contains UPDATED_ERROR_MESSAGE
        defaultNotificationsShouldNotBeFound("errorMessage.contains=" + UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByErrorMessageNotContainsSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where errorMessage does not contain DEFAULT_ERROR_MESSAGE
        defaultNotificationsShouldNotBeFound("errorMessage.doesNotContain=" + DEFAULT_ERROR_MESSAGE);

        // Get all the notificationsList where errorMessage does not contain UPDATED_ERROR_MESSAGE
        defaultNotificationsShouldBeFound("errorMessage.doesNotContain=" + UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where status equals to DEFAULT_STATUS
        defaultNotificationsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the notificationsList where status equals to UPDATED_STATUS
        defaultNotificationsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultNotificationsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the notificationsList where status equals to UPDATED_STATUS
        defaultNotificationsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where status is not null
        defaultNotificationsShouldBeFound("status.specified=true");

        // Get all the notificationsList where status is null
        defaultNotificationsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where creationDate equals to DEFAULT_CREATION_DATE
        defaultNotificationsShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the notificationsList where creationDate equals to UPDATED_CREATION_DATE
        defaultNotificationsShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultNotificationsShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the notificationsList where creationDate equals to UPDATED_CREATION_DATE
        defaultNotificationsShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList where creationDate is not null
        defaultNotificationsShouldBeFound("creationDate.specified=true");

        // Get all the notificationsList where creationDate is null
        defaultNotificationsShouldNotBeFound("creationDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationsShouldBeFound(String filter) throws Exception {
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationsShouldNotBeFound(String filter) throws Exception {
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotifications() throws Exception {
        // Get the notifications
        restNotificationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications
        Notifications updatedNotifications = notificationsRepository.findById(notifications.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotifications are not directly saved in db
        em.detach(updatedNotifications);
        updatedNotifications
            .channel(UPDATED_CHANNEL)
            .channelId(UPDATED_CHANNEL_ID)
            .message(UPDATED_MESSAGE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(updatedNotifications);

        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testNotifications.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testNotifications.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotifications.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testNotifications.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotifications.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationsWithPatch() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications using partial update
        Notifications partialUpdatedNotifications = new Notifications();
        partialUpdatedNotifications.setId(notifications.getId());

        partialUpdatedNotifications.errorMessage(UPDATED_ERROR_MESSAGE).status(UPDATED_STATUS).creationDate(UPDATED_CREATION_DATE);

        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotifications))
            )
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testNotifications.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testNotifications.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotifications.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testNotifications.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotifications.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateNotificationsWithPatch() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications using partial update
        Notifications partialUpdatedNotifications = new Notifications();
        partialUpdatedNotifications.setId(notifications.getId());

        partialUpdatedNotifications
            .channel(UPDATED_CHANNEL)
            .channelId(UPDATED_CHANNEL_ID)
            .message(UPDATED_MESSAGE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotifications))
            )
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testNotifications.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testNotifications.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotifications.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testNotifications.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNotifications.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // Create the Notifications
        NotificationsDTO notificationsDTO = notificationsMapper.toDto(notifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeDelete = notificationsRepository.findAll().size();

        // Delete the notifications
        restNotificationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, notifications.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
