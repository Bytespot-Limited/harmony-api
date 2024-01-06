package tech.bytespot.harmony.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tech.bytespot.harmony.config.SmsProperties;
import tech.bytespot.harmony.domain.Notifications;
import tech.bytespot.harmony.domain.africastalking.SmsResponse;
import tech.bytespot.harmony.repository.NotificationsRepository;
import tech.bytespot.harmony.service.NotificationsService;
import tech.bytespot.harmony.service.dto.NotificationsDTO;
import tech.bytespot.harmony.service.mapper.NotificationsMapper;

/** Service Implementation for managing {@link tech.bytespot.harmony.domain.Notifications}. */
@Service
@Transactional
public class NotificationsServiceImpl implements NotificationsService {

  private final Logger log = LoggerFactory.getLogger(NotificationsServiceImpl.class);

  private final NotificationsRepository notificationsRepository;

  private final NotificationsMapper notificationsMapper;
  private final SmsProperties smsProperties;

  public NotificationsServiceImpl(
      NotificationsRepository notificationsRepository,
      NotificationsMapper notificationsMapper,
      SmsProperties smsProperties) {
    this.notificationsRepository = notificationsRepository;
    this.notificationsMapper = notificationsMapper;
    this.smsProperties = smsProperties;
  }

  @Override
  public NotificationsDTO save(NotificationsDTO notificationsDTO) {
    log.debug("Request to save Notifications : {}", notificationsDTO);
    Notifications notifications = notificationsMapper.toEntity(notificationsDTO);
    notifications = notificationsRepository.save(notifications);
    return notificationsMapper.toDto(notifications);
  }

  @Override
  public NotificationsDTO update(NotificationsDTO notificationsDTO) {
    log.debug("Request to update Notifications : {}", notificationsDTO);
    Notifications notifications = notificationsMapper.toEntity(notificationsDTO);
    notifications = notificationsRepository.save(notifications);
    return notificationsMapper.toDto(notifications);
  }

  @Override
  public Optional<NotificationsDTO> partialUpdate(NotificationsDTO notificationsDTO) {
    log.debug("Request to partially update Notifications : {}", notificationsDTO);

    return notificationsRepository
        .findById(notificationsDTO.getId())
        .map(
            existingNotifications -> {
              notificationsMapper.partialUpdate(existingNotifications, notificationsDTO);

              return existingNotifications;
            })
        .map(notificationsRepository::save)
        .map(notificationsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<NotificationsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Notifications");
    return notificationsRepository.findAll(pageable).map(notificationsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<NotificationsDTO> findOne(Long id) {
    log.debug("Request to get Notifications : {}", id);
    return notificationsRepository.findById(id).map(notificationsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Notifications : {}", id);
    notificationsRepository.deleteById(id);
  }

  /**
   * Send SMS to a given phone number
   *
   * @param message
   * @param guardianPhoneNumber
   */
  public void sendSms(String message, String guardianPhoneNumber) {
    var client =
        WebClient.builder()
            .baseUrl("https://api.africastalking.com/version1/messaging")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("apiKey", smsProperties.getApiKey())
            .build();

    var response =
        client
            .post()
            .body(
                BodyInserters.fromFormData("username", smsProperties.getUsername())
                    .with("to", "+".concat(guardianPhoneNumber))
                    .with("message", message))
            .retrieve()
            .bodyToMono(SmsResponse.class)
            .block();
    log.info("Message sent to AT for the guardian message: {}", response);
  }
}
