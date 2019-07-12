package info.novatec.spring.showcase.search.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
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
@Document(
    indexName = ModelConstants.INDEX_USER,
    type = ModelConstants.TYPE_USER,
    shards = 1,
    replicas = 0,
    refreshInterval = "-1")
public class User implements UserDetails {

  @Id @NonNull @NotNull private UUID identifier;

  @NonNull @NotNull private String idpId;

  private String displayName;

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
