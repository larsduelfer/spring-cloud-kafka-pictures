package info.novatec.spring.showcase.comment.service.assembler;

import info.novatec.spring.showcase.comment.message.CommentAddedEventAvro;
import info.novatec.spring.showcase.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentEventAssembler {

  public CommentAddedEventAvro toCommentAddedEvent(Comment comment) {
    return CommentAddedEventAvro.newBuilder()
        .setIdentifier(comment.getIdentifier().toString())
        .setUserIdentifier(comment.getUserIdentifier().toString())
        .setComment(comment.getComment())
        .setDate(comment.getDate().getTime())
        .build();
  }
}
