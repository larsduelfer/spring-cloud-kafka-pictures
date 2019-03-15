package info.novatec.spring.showcase.resize.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TupleTest {

  /** Verifies that {@link Tuple} is correctly initialized with constructor. */
  @Test
  public void verifyConstructorInitialization() {
    Tuple<String, String> tuple = new Tuple<>("A", "B");
    assertThat(tuple.getFirst()).isEqualTo("A");
    assertThat(tuple.getSecond()).isEqualTo("B");
  }

  /**
   * Verifies that {@link Tuple} is correctly initialized by calling {@link Tuple#of(Object,
   * Object)}.
   */
  @Test
  public void verifyOfInstantiation() {
    Tuple<String, String> tuple = Tuple.of("A", "B");
    assertThat(tuple.getFirst()).isEqualTo("A");
    assertThat(tuple.getSecond()).isEqualTo("B");
  }
}
