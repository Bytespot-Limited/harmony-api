package tech.bytespot.harmony.web.rest.gps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: eli.muraya (https://github.com/elimuraya95))
 * @date: 02/01/2024
 */
@ToString
@NoArgsConstructor
@Getter
@Setter
public class GpsData {
  private String deviceId;
  private String time;
  private String status;
  private String latitude;
  private String longitude;
  private String speedInKnots;
  private String headingDirection;
  private String date;
  private String rfid;
}
