package tech.bytespot.harmony.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bytespot.harmony.domain.StudentTrips;
import tech.bytespot.harmony.domain.enumeration.BoardingStatus;
import tech.bytespot.harmony.domain.enumeration.TripStatus;
import tech.bytespot.harmony.repository.StudentTripsRepository;
import tech.bytespot.harmony.repository.TripsRepository;
import tech.bytespot.harmony.service.StudentTripsService;
import tech.bytespot.harmony.service.dto.StudentTripsDTO;
import tech.bytespot.harmony.service.mapper.StudentTripsMapper;

/** Service Implementation for managing {@link tech.bytespot.harmony.domain.StudentTrips}. */
@Service
@Transactional
public class StudentTripsServiceImpl implements StudentTripsService {

  private final Logger log = LoggerFactory.getLogger(StudentTripsServiceImpl.class);

  private final StudentTripsRepository studentTripsRepository;
  private final TripsRepository tripsRepository;

  private final StudentTripsMapper studentTripsMapper;
  private final NotificationsServiceImpl notificationsService;

  @Value("${app-url}")
  private String appUrl;

  public StudentTripsServiceImpl(
      StudentTripsRepository studentTripsRepository,
      TripsRepository tripsRepository,
      StudentTripsMapper studentTripsMapper,
      NotificationsServiceImpl notificationsService) {
    this.studentTripsRepository = studentTripsRepository;
    this.tripsRepository = tripsRepository;
    this.studentTripsMapper = studentTripsMapper;
    this.notificationsService = notificationsService;
  }

  @Override
  public StudentTripsDTO save(StudentTripsDTO studentTripsDTO) {
    log.debug("Request to save StudentTrips : {}", studentTripsDTO);
    StudentTrips studentTrips = studentTripsMapper.toEntity(studentTripsDTO);
    studentTrips = studentTripsRepository.save(studentTrips);
    return studentTripsMapper.toDto(studentTrips);
  }

  @Override
  public StudentTripsDTO update(StudentTripsDTO studentTripsDTO) {
    log.info("Request to update StudentTrips : {}", studentTripsDTO);
    StudentTrips studentTrips = studentTripsMapper.toEntity(studentTripsDTO);
    var oldRecord = studentTripsRepository.findById(studentTripsDTO.getId());

    studentTrips = studentTripsRepository.save(studentTrips);
    return studentTripsMapper.toDto(studentTrips);
  }

  @Override
  public Optional<StudentTripsDTO> partialUpdate(StudentTripsDTO studentTripsDTO) {
    log.debug("Request to partially update StudentTrips : {}", studentTripsDTO);

    var oldRecord = studentTripsRepository.findById(studentTripsDTO.getId());
    var oldStatus = oldRecord.get().getStatus();
    var newStatus = studentTripsDTO.getStatus();

    var response =
        studentTripsRepository
            .findById(studentTripsDTO.getId())
            .map(
                existingStudentTrips -> {
                  studentTripsMapper.partialUpdate(existingStudentTrips, studentTripsDTO);

                  return existingStudentTrips;
                })
            .map(studentTripsRepository::save)
            .map(studentTripsMapper::toDto);

    // Send SMS notification to the guardian
    this.sendNotification(
        oldStatus,
        response.get().getStudent().getGuardian().getPhoneNumber(),
        newStatus,
        response.get());

    // Check if the trip has more students
    this.checkTripIsOngoing(oldRecord.get());
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StudentTripsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all StudentTrips");
    return studentTripsRepository.findAll(pageable).map(studentTripsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<StudentTripsDTO> findOne(Long id) {
    log.debug("Request to get StudentTrips : {}", id);
    return studentTripsRepository.findById(id).map(studentTripsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete StudentTrips : {}", id);
    studentTripsRepository.deleteById(id);
  }

  /**
   * & Send sms to the guardian
   *
   * @param oldRecordStatus
   * @param guardianPhoneNumber
   * @param newRecordStatus
   * @param studentTrips
   */
  private void sendNotification(
      BoardingStatus oldRecordStatus,
      String guardianPhoneNumber,
      BoardingStatus newRecordStatus,
      StudentTripsDTO studentTrips) {
    log.info("Old record status: {} .Current record status: {} ", oldRecordStatus, newRecordStatus);
    // Check if the old record is PENDING, so that we can send a message
    if (oldRecordStatus.equals(BoardingStatus.PENDING)
        && newRecordStatus.equals(BoardingStatus.BOARDED)) {
      // Send a message to the guardian to notify the student is boarded
      String tripUrl =
          appUrl
              .concat("terminals/")
              .concat(String.valueOf(studentTrips.getStudent().getFleet().getTerminal().getId()));
      StringBuilder message = new StringBuilder();
      message
          .append("Hello ")
          .append(studentTrips.getStudent().getGuardian().getName())
          .append(",\n");

      message
          .append("Student ")
          .append(studentTrips.getStudent().getName())
          .append(" has boarded the bus ")
          .append(studentTrips.getStudent().getFleet().getNumberPlate());

      switch (studentTrips.getTrip().getTripType()) {
        case PICKUP:
          message.append(" headed to school").append(".");
          message.append("\n");
          break;
        case DROPOFF:
          message.append(" headed home").append(".");
          message.append("\n");
          break;
      }

      message.append("Follow the trip here: ").append(tripUrl);
      notificationsService.sendSms(message.toString(), guardianPhoneNumber);

      // Persist the message to the SMS

    }
    if (oldRecordStatus.equals(BoardingStatus.BOARDED)
        && newRecordStatus.equals(BoardingStatus.DROPPED_OFF)) {
      // Send a message to the guardian to notify the student is DROPPED OFF
      StringBuilder message = new StringBuilder();
      message
          .append("Hello ")
          .append(studentTrips.getStudent().getGuardian().getName())
          .append(",\n");

      message
          .append("Student ")
          .append(studentTrips.getStudent().getName())
          .append(" has been dropped from the bus ")
          .append(studentTrips.getStudent().getFleet().getNumberPlate());

      switch (studentTrips.getTrip().getTripType()) {
        case PICKUP:
          message.append(" at school").append(".");
          message.append("\n");
          break;
        case DROPOFF:
          message.append(" at home").append(".");
          message.append("\n");
          break;
      }
      notificationsService.sendSms(message.toString(), guardianPhoneNumber);

      // Persist the message to the SMS
    }
    log.info("Old record status: {} .Current record status: {} ", oldRecordStatus, newRecordStatus);
  }

  /**
   * Update the trip status, if no students are left in the trip
   *
   * @param studentTrip
   */
  private void checkTripIsOngoing(StudentTrips studentTrip) {
    var pendingStudentsInTrip =
        studentTripsRepository.findAllByTripAndStatusIn(
            studentTrip.getTrip(), List.of(BoardingStatus.PENDING, BoardingStatus.BOARDED));
    log.info("Students pending in the trip: {}", pendingStudentsInTrip.size());
    if (pendingStudentsInTrip.isEmpty()) {
      log.info("No pending students on the trip. Closing the trip ...");
      var trip = tripsRepository.findById(studentTrip.getTrip().getId());
      trip.get().setTripStatus(TripStatus.COMPLETED);
      trip.get().setEndTime(Instant.now());
      trip.get().setModifiedDate(Instant.now());
      var updatedTrip = tripsRepository.save(trip.get());
      log.info("Updated trip details: {}", updatedTrip);
    }
  }
}
