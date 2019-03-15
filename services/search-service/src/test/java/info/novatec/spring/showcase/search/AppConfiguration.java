package info.novatec.spring.showcase.search;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/** Configuration to disable spring cloud stream dependencies. */
@EnableAutoConfiguration(exclude = {EurekaClientAutoConfiguration.class})
@Configuration
class AppConfiguration {}
