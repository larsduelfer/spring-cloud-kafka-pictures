package info.novatec.spring.showcase.search.configuration.api;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * Register a {@link ForwardedHeaderFilter} to apply the x-forwarded-* HTTP headers in the URI
 * builders.
 */
@Configuration
public class ForwardedHeaderFilterConfiguration {

  @Bean
  public FilterRegistrationBean forwardedHeaderFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return filterRegistrationBean;
  }
}
