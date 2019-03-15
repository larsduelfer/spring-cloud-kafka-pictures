package info.novatec.spring.showcase.comment.api.assembler;

import info.novatec.spring.showcase.comment.configuration.security.SecurityContextHelper;
import info.novatec.spring.showcase.comment.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.util.Date;
import java.util.UUID;

@Component
public class CommentAssembler {

  @Autowired private IdGenerator idGenerator;

  public Comment assemble(UUID imageIdentifier, String text) {
    return new Comment(
        idGenerator.generateId(),
        imageIdentifier,
        SecurityContextHelper.getInstance().getCurrentUser().getIdentifier(),
        text,
        new Date());
  }
}
