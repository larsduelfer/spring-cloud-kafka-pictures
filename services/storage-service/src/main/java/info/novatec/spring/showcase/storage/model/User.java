package info.novatec.spring.showcase.storage.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@Document
public class User implements UserDetails {

  @Indexed(unique = true, name = "UK_User_Identifier")
  @NonNull
  @NotNull
  private UUID identifier;

  @Indexed(unique = true, name = "UK_User_IdpId")
  @NonNull
  @NotNull
  private String idpId;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList("ROLE_USER");
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return idpId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
