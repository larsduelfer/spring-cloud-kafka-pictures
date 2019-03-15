package info.novatec.spring.showcase.search.api.assembler;

import info.novatec.spring.showcase.search.model.User;
import info.novatec.spring.showcase.user.message.v1.resource.UserCreatedEvent;
import info.novatec.spring.showcase.user.message.v1.resource.UserUpdatedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

  public User assemble(UserCreatedEvent userCreatedEvent) {
    User user = new User();
    user.setIdentifier(userCreatedEvent.getIdentifier());
    user.setUserId(userCreatedEvent.getUserId());
    return user;
  }

  public User assemble(UserUpdatedEvent userUpdatedEvent) {
    User user = new User();
    user.setIdentifier(userUpdatedEvent.getIdentifier());
    user.setUserId(userUpdatedEvent.getUserId());
    user.setDisplayName(userUpdatedEvent.getDisplayName());
    return user;
  }
}
