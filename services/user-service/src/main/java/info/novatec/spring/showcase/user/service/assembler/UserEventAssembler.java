package info.novatec.spring.showcase.user.service.assembler;

import info.novatec.spring.showcase.user.message.UserCreatedEventAvro;
import info.novatec.spring.showcase.user.message.UserUpdatedEventAvro;
import info.novatec.spring.showcase.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserEventAssembler {

  public UserCreatedEventAvro toCreatedEvent(User user) {
    return UserCreatedEventAvro.newBuilder()
        .setDate(user.getCreatedDate().getTime())
        .setIdentifier(user.getIdentifier().toString())
        .setIdpId(user.getIdpId())
        .build();
  }

  public UserUpdatedEventAvro toUpdatedEvent(User user) {
    return UserUpdatedEventAvro.newBuilder()
        .setDate(user.getLastModifiedDate().getTime())
        .setDisplayName(user.getDisplayName())
        .setFirstName(user.getFirstName())
        .setIdentifier(user.getIdentifier().toString())
        .setIdpId(user.getIdpId())
        .setLastName(user.getLastName())
        .build();
  }
}
