package info.novatec.spring.showcase.comment.api.resource;

import org.springframework.hateoas.PagedResources;

import java.util.Collection;

public class UserPageResource extends PagedResource<UserResource> {

  public UserPageResource(
      Collection<UserResource> userResources, PagedResources.PageMetadata pageMetadata) {
    super(userResources, pageMetadata);
  }
}
