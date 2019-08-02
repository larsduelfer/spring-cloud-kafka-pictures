package info.novatec.spring.showcase.user.model;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(
    name = "user_entity",
    uniqueConstraints = {
      @UniqueConstraint(name = "UK_User_Identifier", columnNames = "identifier")
    },
    indexes = {@Index(name = "UK_User_IdpId", columnList = "idpid", unique = true)})
public class User extends AbstractPersistable<Long> implements UserDetails {

  @Version private Long version;

  @NonNull
  @NotNull
  @Column(nullable = false)
  private Date createdDate;

  @NonNull
  @NotNull
  @Column(nullable = false)
  private Date lastModifiedDate;

  @NonNull
  @NotNull
  @Column(nullable = false)
  private UUID identifier;

  @NonNull
  @NotNull
  @Column(nullable = false)
  private String idpId;

  @Column(nullable = false)
  private boolean registered;

  private String firstName;

  private String lastName;

  private String displayName;

  public User(@NonNull @NotNull UUID identifier, @NonNull @NotNull String idpId, @NonNull @NotNull Date date) {
    this.identifier = identifier;
    this.idpId = idpId;
    this.createdDate = date;
    this.lastModifiedDate = date;
  }

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

  public boolean isRegistered() {
    return registered;
  }
}
