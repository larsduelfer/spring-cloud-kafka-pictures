package info.novatec.spring.showcase.user.configuration.security;

import info.novatec.spring.showcase.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.IdGenerator;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired private IdGenerator idGenerator;

  @Autowired private UserService userService;

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private OAuth2ResourceServerProperties resourceServerProperties;

  public void configure(HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .requestMatchers(
            PathRequest.toStaticResources().at(StaticResourceLocation.FAVICON),
            EndpointRequest.toAnyEndpoint())
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(
            new AuthenticationTokenConverter(
                idGenerator,
                userService,
                userDetailsService,
                resourceServerProperties.getJwt().getIssuerUri()));
  }
}
