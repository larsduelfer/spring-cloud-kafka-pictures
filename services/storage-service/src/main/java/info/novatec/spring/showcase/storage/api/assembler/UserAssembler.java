package info.novatec.spring.showcase.storage.api.assembler;

import info.novatec.spring.showcase.storage.model.User;
import info.novatec.spring.showcase.user.message.v1.resource.UserCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

  public User assemble(UserCreatedEvent userCreatedEvent) {
    User user = new User();
    user.setIdentifier(userCreatedEvent.getIdentifier());
    user.setUserId(userCreatedEvent.getUserId());
    return user;
  }
}
