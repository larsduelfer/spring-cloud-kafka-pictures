package info.novatec.spring.showcase.resize.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tuple<T, U> {

  private T first;
  private U second;

  /**
   * Gets tuple of specified data.
   *
   * @param first first attribute
   * @param second second attribute
   * @param <S> type of first attribute
   * @param <T> type of second attribute
   * @return tuple
   */
  @SuppressWarnings("unchecked")
  public static <S, T> Tuple<S, T> of(S first, T second) {
    return new Tuple(first, second);
  }
}
