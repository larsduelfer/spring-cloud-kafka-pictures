package info.novatec.spring.showcase.storage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

/** Test to verify successful startup of application context. */
@RunWith(SpringRunner.class)
@DataMongoTest
public class StorageServiceApplicationTests {

  /** Verifies that context starts successfully. */
  @Test
  public void contextLoads() {}
}
