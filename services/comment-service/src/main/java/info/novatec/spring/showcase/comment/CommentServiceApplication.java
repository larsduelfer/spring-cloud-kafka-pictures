package info.novatec.spring.showcase.comment;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@EnableEurekaClient
@SpringBootApplication
public class CommentServiceApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(CommentServiceApplication.class, args);
  }

  @Bean
  public Sampler defaultSampler() {
    return Sampler.ALWAYS_SAMPLE;
  }
}
