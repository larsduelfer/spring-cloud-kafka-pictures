package info.novatec.spring.showcase.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/** Test to verify successful startup of application context. */
@RunWith(SpringRunner.class)
@ActiveProfiles({"file-local", "native"})
@SpringBootTest
public class ConfigApplicationTests {

  /** Verifies that context starts successfully. */
  @Test
  public void contextLoads() {}
}
