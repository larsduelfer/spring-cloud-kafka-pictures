package info.novatec.spring.showcase.comment.configuration.security;

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

import java.util.Map;

public class AuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenConverter.class);

  private static final String SUBJECT = "sub";

  private static final String ISSUER = "iss";

  private final UserDetailsService userDetailsService;

  private final String issuer;

  AuthenticationTokenConverter(UserDetailsService userDetailsService, String issuer) {
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
      UserDetails user = userDetailsService.loadUserByUsername(userId);
      return new UsernamePasswordAuthenticationToken(user, "n/a", user.getAuthorities());
    } catch (UsernameNotFoundException ex) {
      LOGGER.warn("User not found", ex);
      throw new BadCredentialsException("Bad credentials");
    }
  }
}
