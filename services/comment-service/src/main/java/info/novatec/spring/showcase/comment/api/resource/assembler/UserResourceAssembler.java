package info.novatec.spring.showcase.comment.api.resource.assembler;

import info.novatec.spring.showcase.comment.api.resource.UserResource;
import info.novatec.spring.showcase.comment.model.User;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserResourceAssembler {

  UserResource assemble(List<User> users, UUID userIdentifier) {
    return assemble(
        users.stream().filter(user -> user.getIdentifier().equals(userIdentifier)).findFirst());
  }

  private UserResource assemble(Optional<User> optionalUser) {
    UserResource userResource = new UserResource();
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      userResource.setIdentifier(user.getIdentifier());
      userResource.setDisplayName(user.getDisplayName());
      userResource.add(
          BasicLinkBuilder.linkToCurrentMapping()
              .slash("/users/binaries/" + userResource.getIdentifier())
              .withRel("profilePictureUrl"));
    }
    return userResource;
  }
}
