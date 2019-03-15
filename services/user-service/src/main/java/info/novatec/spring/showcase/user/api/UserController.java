package info.novatec.spring.showcase.user.api;

import info.novatec.spring.showcase.user.api.resource.SaveUserResource;
import info.novatec.spring.showcase.user.api.resource.UserResource;
import info.novatec.spring.showcase.user.api.resource.assembler.UserResourceAssembler;
import info.novatec.spring.showcase.user.model.User;
import info.novatec.spring.showcase.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(
    path = UserController.USER_RESOURCE_PATH,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

  static final String USER_RESOURCE_PATH = "/users";

  @Autowired private UserService userService;

  @Autowired private UserResourceAssembler userResourceAssembler;

  @RequestMapping(
      path = "/current",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> updateUser(
      @AuthenticationPrincipal(errorOnInvalidType = true) User currentUser,
      @Valid @RequestBody SaveUserResource saveUserResource) {

    User updatedUser =
        userService.updateUser(
            currentUser,
            saveUserResource.getDisplayName(),
            saveUserResource.getFirstName(),
            saveUserResource.getLastName(),
            saveUserResource.getVersion());
    return ResponseEntity.ok().body(userResourceAssembler.toResource(updatedUser));
  }

  @RequestMapping(path = "/current", method = RequestMethod.GET)
  public UserResource findOne(
      @AuthenticationPrincipal(errorOnInvalidType = true) User currentUser) {
    return userResourceAssembler.toResource(currentUser);
  }
}
