package tech.bytespot.harmony.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: eli.muraya (https://github.com/elimuraya95))
 * @date: 30/12/2023
 */
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
  private String username;
  private String apiKey;

  public String getUsername() {
    return username;
  }

  public String getApiKey() {
    return apiKey;
  }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
