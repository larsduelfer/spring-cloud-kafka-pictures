package info.novatec.spring.showcase.user.api.resource.assembler;

import info.novatec.spring.showcase.user.api.UserController;
import info.novatec.spring.showcase.user.api.resource.UserResource;
import info.novatec.spring.showcase.user.model.User;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, UserResource> {
  @Override
  public UserResource toResource(User user) {
    UserResource userResource =
        new UserResource(
            user.getIdentifier(),
            user.getVersion(),
            user.getFirstName(),
            user.getLastName(),
            user.getDisplayName(),
            user.isRegistered());

    userResource.add(linkTo(methodOn(UserController.class).findOne(null)).withSelfRel());
    return userResource;
  }
}
