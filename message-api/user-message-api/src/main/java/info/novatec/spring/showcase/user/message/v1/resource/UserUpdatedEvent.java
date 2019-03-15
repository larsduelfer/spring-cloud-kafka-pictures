package info.novatec.spring.showcase.user.message.v1.resource;

import info.novatec.spring.showcase.common.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdatedEvent extends Event {

  public static final int SCHEMA_VERSION = 1;

  private String userId;

  private String firstName;

  private String lastName;

  private String displayName;

  public UserUpdatedEvent(
      Date date,
      UUID identifier,
      String userId,
      String firstName,
      String lastName,
      String displayName) {
    super(SCHEMA_VERSION, identifier, date);
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.displayName = displayName;
  }
}
