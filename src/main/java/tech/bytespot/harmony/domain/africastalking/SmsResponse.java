package tech.bytespot.harmony.domain.africastalking;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: eli.muraya (https://github.com/elimuraya95))
 * @date: 30/12/2023
 */
@NoArgsConstructor
@Getter
@Setter
public class SmsResponse {

  @JsonProperty("SMSMessageData")
  private MessageData messageData;

  @NoArgsConstructor
  @Getter
  @Setter
  private static class MessageData {
    @JsonProperty("Message")
    private String message;

    @JsonProperty("Recipients")
    private List<Recipient> recipients;
  }

  @NoArgsConstructor
  @Getter
  @Setter
  private static class Recipient {
    private String cost;
    private String messageId;
    private int messageParts;
    private String number;
    private String status;
    private Integer statusCode;
  }
}
