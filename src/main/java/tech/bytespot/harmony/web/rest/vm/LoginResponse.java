package tech.bytespot.harmony.web.rest.vm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: eli.muraya (https://github.com/elimuraya95))
 * @date: 03/01/2024
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
  private int code;
  private String message;
  private String token;
}
