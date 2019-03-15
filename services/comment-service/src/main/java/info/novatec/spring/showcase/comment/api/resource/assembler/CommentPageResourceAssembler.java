package info.novatec.spring.showcase.comment.api.resource.assembler;

import info.novatec.spring.showcase.comment.api.resource.CommentPageResource;
import info.novatec.spring.showcase.comment.api.resource.CommentResource;
import info.novatec.spring.showcase.comment.model.Comment;
import info.novatec.spring.showcase.comment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentPageResourceAssembler {

  @Autowired private CommentResourceAssembler commentResourceAssembler;

  public CommentPageResource assemble(Page<Comment> comments, List<User> users) {
    List<CommentResource> commentResources =
        comments.stream()
            .map(comment -> commentResourceAssembler.assemble(comment, users))
            .collect(Collectors.toList());
    return new CommentPageResource(
        commentResources,
        new PagedResources.PageMetadata(
            comments.getSize(),
            comments.getNumber(),
            comments.getTotalElements(),
            comments.getTotalPages()));
  }
}
