package info.novatec.spring.showcase.storage.configuration.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class DefaultRetryListener implements RetryListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRetryListener.class);

  @Override
  public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
    // Do nothing and allow retry
    return true;
  }

  @Override
  public <T, E extends Throwable> void close(
      RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    // Do nothing
  }

  @Override
  public <T, E extends Throwable> void onError(
      RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    LOGGER.debug("Retry #" + context.getRetryCount() + " failed.");
  }
}
