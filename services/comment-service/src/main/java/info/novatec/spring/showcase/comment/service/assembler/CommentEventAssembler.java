package info.novatec.spring.showcase.comment.service.assembler;

import info.novatec.spring.showcase.comment.message.v1.resource.CommentAddedEvent;
import info.novatec.spring.showcase.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentEventAssembler {

  public CommentAddedEvent toCommentAddedEvent(Comment comment) {
    return new CommentAddedEvent(
        comment.getDate(),
        comment.getIdentifier(),
        comment.getUserIdentifier(),
        comment.getComment());
  }
}
