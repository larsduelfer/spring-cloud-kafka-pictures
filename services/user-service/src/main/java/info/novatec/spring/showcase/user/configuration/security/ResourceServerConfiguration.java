package info.novatec.spring.showcase.user.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Autowired private ResourceServerProperties resource;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(this.resource.getResourceId());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }
}
