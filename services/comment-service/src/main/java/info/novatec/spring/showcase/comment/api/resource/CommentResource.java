package info.novatec.spring.showcase.comment.api.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentResource extends HalResource {

  private UUID identifier;
  private String comment;
}
