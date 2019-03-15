package info.novatec.spring.showcase.user.service.assembler;

import info.novatec.spring.showcase.user.message.v1.resource.UserCreatedEvent;
import info.novatec.spring.showcase.user.message.v1.resource.UserUpdatedEvent;
import info.novatec.spring.showcase.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserEventAssembler {

  public UserCreatedEvent toCreatedEvent(User user) {
    return new UserCreatedEvent(user.getCreatedDate(), user.getIdentifier(), user.getUserId());
  }

  public UserUpdatedEvent toUpdatedEvent(User user) {
    return new UserUpdatedEvent(
        user.getLastModifiedDate(),
        user.getIdentifier(),
        user.getUserId(),
        user.getFirstName(),
        user.getLastName(),
        user.getDisplayName());
  }
}
