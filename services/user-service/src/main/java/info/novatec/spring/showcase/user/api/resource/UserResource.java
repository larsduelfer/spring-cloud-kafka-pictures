package info.novatec.spring.showcase.user.api.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserResource extends ResourceSupport {

  private UUID identifier;

  private Long version;

  private String firstName;

  private String lastName;

  private String displayName;

  private boolean registered;
}
