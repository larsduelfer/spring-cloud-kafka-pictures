package info.novatec.spring.showcase.comment.api.resource.assembler;

import info.novatec.spring.showcase.comment.api.resource.CommentResource;
import info.novatec.spring.showcase.comment.model.Comment;
import info.novatec.spring.showcase.comment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentResourceAssembler {

  @Autowired private UserResourceAssembler userResourceAssembler;

  public CommentResource assemble(Comment comment, List<User> users) {
    CommentResource commentResource = new CommentResource();
    commentResource.setIdentifier(comment.getIdentifier());
    commentResource.setComment(comment.getComment());
    commentResource.embed(
        "user", userResourceAssembler.assemble(users, comment.getUserIdentifier()));
    return commentResource;
  }
}
