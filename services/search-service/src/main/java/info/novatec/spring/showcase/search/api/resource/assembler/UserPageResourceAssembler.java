package info.novatec.spring.showcase.search.api.resource.assembler;

import info.novatec.spring.showcase.search.api.resource.UserPageResource;
import info.novatec.spring.showcase.search.api.resource.UserResource;
import info.novatec.spring.showcase.search.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserPageResourceAssembler {

  @Autowired private UserResourceAssembler userResourceAssembler;

  public UserPageResource assemble(Page<User> users) {
    List<UserResource> userResources =
        users.stream()
            .map(user -> userResourceAssembler.assemble(Optional.of(user)))
            .collect(Collectors.toList());
    return new UserPageResource(
        userResources,
        new PagedResources.PageMetadata(
            users.getSize(), users.getNumber(), users.getTotalElements(), users.getTotalPages()));
  }
}
