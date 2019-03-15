package info.novatec.spring.showcase.resize.configuration.messaging;

import info.novatec.spring.showcase.common.Event;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

public class DefaultRecoveryCallback implements RecoveryCallback<Object> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRecoveryCallback.class);

  @Override
  public Object recover(RetryContext context) {
    String message = context.getLastThrowable().getMessage();
    ConsumerRecord<String, Event> record = getConsumerRecord(context);
    LOGGER.error(
        "Retry failed after {} attempts. Topic: {} Partition: {} Offset: {} Message: {}",
        context.getRetryCount(),
        record.topic(),
        record.partition(),
        record.offset(),
        message);
    // Throw an exception to prevent the acknowledgement of the record
    throw new RetriesFailedException(context.getLastThrowable());
  }

  private static ConsumerRecord<String, Event> getConsumerRecord(RetryContext context) {
    Object contextRecord = context.getAttribute(RetryingMessageListenerAdapter.CONTEXT_RECORD);
    if (contextRecord == null) {
      throw new IllegalStateException("Context record not found in retry context");
    }
    if (!(contextRecord instanceof ConsumerRecord)) {
      throw new IllegalStateException(
          "Context record not of expected type ConsumerRecord, but: "
              + contextRecord.getClass().getSimpleName());
    }
    return (ConsumerRecord<String, Event>) contextRecord;
  }

  private static class RetriesFailedException extends RuntimeException {

    RetriesFailedException(Throwable cause) {
      super(cause);
    }
  }
}
