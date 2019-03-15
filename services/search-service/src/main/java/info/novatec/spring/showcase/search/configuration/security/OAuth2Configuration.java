package info.novatec.spring.showcase.search.configuration.security;

import info.novatec.spring.showcase.search.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

@EnableResourceServer
@Configuration
public class OAuth2Configuration {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Configuration.class);
  private static final String SUBJECT = "sub";
  private static final String ISSUER = "iss";

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private UserService userService;

  @Value("${oauth.issuer}")
  private String issuer;

  @Bean
  public JwtAccessTokenConverterConfigurer jwtAccessTokenConverterConfigurer() {
    return converter -> {
      DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
      defaultAccessTokenConverter.setUserTokenConverter(
          new UserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
              Map<String, Object> response = new HashMap<>();
              response.put(USERNAME, authentication.getName());
              response.put(
                  AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
              return response;
            }

            @Override
            public Authentication extractAuthentication(Map<String, ?> map) {
              String issuerValue = (String) map.get(ISSUER);

              if (!issuer.equalsIgnoreCase(issuerValue)) {
                LOGGER.error("Unknown issuer {}", issuerValue);
                throw new BadClientCredentialsException();
              }

              String userId = StringUtils.trimToNull((String) map.get(SUBJECT));
              try {
                UserDetails user = userDetailsService.loadUserByUsername(userId);
                return getAuthentication(user);
              } catch (UsernameNotFoundException ex) {
                LOGGER.warn("User not found", ex);
                throw new BadClientCredentialsException();
              }
            }
          });
      converter.setAccessTokenConverter(defaultAccessTokenConverter);
    };
  }

  private Authentication getAuthentication(UserDetails user) {
    return new UsernamePasswordAuthenticationToken(user, "n/a", user.getAuthorities());
  }
}
