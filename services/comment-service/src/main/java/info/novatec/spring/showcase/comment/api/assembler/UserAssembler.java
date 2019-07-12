package info.novatec.spring.showcase.comment.api.assembler;

import info.novatec.spring.showcase.comment.model.User;
import info.novatec.spring.showcase.user.message.UserCreatedEventAvro;
import info.novatec.spring.showcase.user.message.UserUpdatedEventAvro;
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

  public User assemble(UserUpdatedEventAvro userUpdatedEvent) {
    User user = new User();
    user.setIdentifier(UUID.fromString(userUpdatedEvent.getIdentifier()));
    user.setIdpId(userUpdatedEvent.getIdpId());
    user.setDisplayName(userUpdatedEvent.getDisplayName());
    return user;
  }
}
