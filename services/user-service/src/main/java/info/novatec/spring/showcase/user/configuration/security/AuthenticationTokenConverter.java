package info.novatec.spring.showcase.user.configuration.security;

import info.novatec.spring.showcase.user.model.User;
import info.novatec.spring.showcase.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.util.Assert;
import org.springframework.util.IdGenerator;

import java.util.Date;
import java.util.Map;

public class AuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenConverter.class);

  private static final String SUBJECT = "sub";

  private static final String ISSUER = "iss";

  private final IdGenerator idGenerator;

  private final UserService userService;

  private final UserDetailsService userDetailsService;

  private final String issuer;

  AuthenticationTokenConverter(
      IdGenerator idGenerator,
      UserService userService,
      UserDetailsService userDetailsService,
      String issuer) {
    this.idGenerator = idGenerator;
    this.userService = userService;
    this.userDetailsService = userDetailsService;
    this.issuer = issuer;
  }

  @Override
  public AbstractAuthenticationToken convert(Jwt source) {
    Assert.notNull(source, "JWT must not be null");
    Map<String, ?> claims = source.getClaims();

    Object issuerValue = claims.get(ISSUER);

    if (issuerValue == null || !issuer.equalsIgnoreCase(issuerValue.toString())) {
      LOGGER.error("Invalid issuer {}", issuerValue);
      throw new OAuth2AuthenticationException(
          new BearerTokenError("invalid_token", HttpStatus.UNAUTHORIZED, "Invalid issuer", null));
    }

    Object subjectValue = claims.get(SUBJECT);

    if (!(subjectValue instanceof String)) {
      LOGGER.error("Invalid subject {}", subjectValue);
      throw new OAuth2AuthenticationException(
          new BearerTokenError("invalid_token", HttpStatus.UNAUTHORIZED, "Invalid subject", null));
    }

    String userId = StringUtils.trimToNull((String) subjectValue);
    try {
      return getAuthentication(userDetailsService.loadUserByUsername(userId));
    } catch (UsernameNotFoundException ex) {
      if (userId != null) {
        return getAuthentication(
            userService.createUser(new User(idGenerator.generateId(), userId, new Date())));
      }
      LOGGER.warn("User not found", ex);
      throw new BadCredentialsException("Bad credentials");
    }
  }

  private AbstractAuthenticationToken getAuthentication(UserDetails user) {
    return new UsernamePasswordAuthenticationToken(user, "n/a", user.getAuthorities());
  }
}
