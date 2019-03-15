package info.novatec.spring.showcase.gateway;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(GatewayApplication.class, args);
  }

  @Bean
  public Sampler defaultSampler() {
    return Sampler.ALWAYS_SAMPLE;
  }
}
