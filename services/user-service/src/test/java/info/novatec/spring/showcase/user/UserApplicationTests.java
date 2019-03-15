package info.novatec.spring.showcase.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/** Test to verify successful startup of application context. */
@ActiveProfiles({"jpa", "db-h2"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppConfiguration.class)
public class UserApplicationTests {

  /** Verifies that context starts successfully. */
  @Test
  public void contextLoads() {}
}
