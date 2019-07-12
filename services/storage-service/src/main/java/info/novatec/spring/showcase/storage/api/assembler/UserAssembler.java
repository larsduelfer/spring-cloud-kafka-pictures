package info.novatec.spring.showcase.storage.api.assembler;

import info.novatec.spring.showcase.storage.model.User;
import info.novatec.spring.showcase.user.message.UserCreatedEventAvro;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAssembler {

  public User assemble(UserCreatedEventAvro userCreatedEvent) {
    User user = new User();
    user.setIdentifier(UUID.fromString(userCreatedEvent.getIdentifier()));
    user.setIdpId(userCreatedEvent.getIdpId());
    return user;
  }
}
