package info.novatec.spring.showcase.comment.api.resource;

import org.springframework.hateoas.PagedResources;

import java.util.Collection;

public class CommentPageResource extends PagedResource<CommentResource> {

  public CommentPageResource(
      Collection<CommentResource> commentResources, PagedResources.PageMetadata pageMetadata) {
    super(commentResources, pageMetadata);
  }
}
