package info.novatec.spring.showcase;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/** Configuration to disable eureka client dependencies. */
@EnableAutoConfiguration(exclude = {EurekaClientAutoConfiguration.class})
@Configuration
class AppConfiguration {}
