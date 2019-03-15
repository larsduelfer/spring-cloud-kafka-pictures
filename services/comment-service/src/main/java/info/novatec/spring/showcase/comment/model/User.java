package info.novatec.spring.showcase.comment.model;

import com.google.common.collect.Lists;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@Table
public class User implements UserDetails {

  @PrimaryKey @NonNull @NotNull private UUID identifier;

  @Indexed @NonNull @NotNull private String userId;

  private String displayName;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return userId;
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
