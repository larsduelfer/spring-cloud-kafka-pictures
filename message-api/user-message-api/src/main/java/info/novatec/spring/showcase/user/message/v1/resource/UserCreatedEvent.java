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
public class UserCreatedEvent extends Event {

  public static final int SCHEMA_VERSION = 1;

  private String userId;

  public UserCreatedEvent(Date date, UUID identifier, String userId) {
    super(SCHEMA_VERSION, identifier, date);
    this.userId = userId;
  }
}
