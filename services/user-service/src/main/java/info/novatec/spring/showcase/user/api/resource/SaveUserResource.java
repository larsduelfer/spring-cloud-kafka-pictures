package info.novatec.spring.showcase.user.api.resource;

import lombok.Data;

@Data
public class SaveUserResource {

  private Long version;

  private String displayName;

  private String firstName;

  private String lastName;
}
