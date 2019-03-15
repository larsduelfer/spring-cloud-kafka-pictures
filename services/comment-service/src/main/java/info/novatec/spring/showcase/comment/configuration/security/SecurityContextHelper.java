package info.novatec.spring.showcase.comment.configuration.security;

import info.novatec.spring.showcase.comment.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SecurityContextHelper {

  private static final String ROLE_PREFIX = "ROLE_";

  private static final SecurityContextHelper INSTANCE = new SecurityContextHelper();

  private SecurityContextHelper() {
    super();
  }

  public static SecurityContextHelper getInstance() {
    return INSTANCE;
  }

  public User getCurrentUserIfLoggedIn() {
    try {
      return getCurrentUser();
    } catch (InsufficientAuthenticationException e) {
      return null;
    }
  }

  public User getCurrentUser() {
    Authentication authentication = getAuthentication();
    if (authentication == null
        || authentication.getPrincipal() != null
            && !User.class.equals(authentication.getPrincipal().getClass())) {
      throw new InsufficientAuthenticationException("Fully authenticated user is required");
    }
    return (User) authentication.getPrincipal();
  }

  public boolean hasRole(String role) {
    return hasAnyRole(role);
  }

  public boolean hasAnyRole(String... roles) {
    return hasAnyAuthorityName(roles);
  }

  private boolean hasAnyAuthorityName(String... roles) {
    Set<String> roleSet = getUserAuthorities();

    for (String role : roles) {
      String defaultedRole = getRoleWithPrefix(role);
      if (roleSet.contains(defaultedRole)) {
        return true;
      }
    }

    return false;
  }

  private String getRoleWithPrefix(String role) {
    if (role == null) {
      return null;
    } else if (role.startsWith(ROLE_PREFIX)) {
      return role;
    }
    return ROLE_PREFIX + role;
  }

  private Set<String> getUserAuthorities() {
    Authentication authentication = getAuthentication();
    if (authentication == null) {
      return new HashSet<>();
    } else {
      Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
      return AuthorityUtils.authorityListToSet(userAuthorities);
    }
  }

  private Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken)) {
      return authentication;
    } else {
      return null;
    }
  }
}
