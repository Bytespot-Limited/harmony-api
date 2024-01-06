package tech.bytespot.harmony.web.rest.gps;

import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bytespot.harmony.repository.TerminalRepository;

/**
 * @author: eli.muraya (https://github.com/elimuraya95))
 * @date: 02/01/2024
 */
@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DeviceGpsCoordinates {

  private final TerminalRepository terminalRepository;

  /**
   * Process GPS data
   *
   * @param gpsData
   * @return
   * @throws URISyntaxException
   */
  @PostMapping("/gps-coordinates")
  public ResponseEntity<GpsData> processGpsCoordinates(@Valid @RequestBody GpsData gpsData) {
    log.info("Received GPS coordinates : {}", gpsData);
    var terminal = terminalRepository.findOneByDevideId(gpsData.getDeviceId());
    if (terminal.isPresent()) {
      if (!ObjectUtils.isEmpty(gpsData.getLatitude())
          && !ObjectUtils.isEmpty(gpsData.getLongitude())) {
        terminal.get().setLatitude(gpsData.getLatitude());
        terminal.get().setLongitude(gpsData.getLongitude());
        terminal.get().setLastPing(Instant.now());
        terminal.get().setModifiedDate(Instant.now());
        var updatedRecord = terminalRepository.save(terminal.get());
        log.info("Updated GPS coordinates for terminal: {}", updatedRecord);
      } else {
        log.info("GPS data received is incorrect: {}", gpsData);
      }
    }else {
        log.info("Terminal with ID {} not found", gpsData);
    }

    return ResponseEntity.ok().body(gpsData);
  }
}
