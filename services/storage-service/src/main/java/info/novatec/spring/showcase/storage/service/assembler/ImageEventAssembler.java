package info.novatec.spring.showcase.storage.service.assembler;

import info.novatec.spring.showcase.image.message.ImageDeletedEventAvro;
import info.novatec.spring.showcase.image.message.ImageUploadedEventAvro;
import info.novatec.spring.showcase.model.Image;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ImageEventAssembler {

  public ImageUploadedEventAvro toUploadedEvent(Image image, String title) {
    return ImageUploadedEventAvro.newBuilder()
        .setDate(image.getCreatedDate().getTime())
        .setIdentifier(image.getImageId().toString())
        .setUserIdentifier(image.getUserId().toString())
        .setTitle(title)
        .build();
  }

  public ImageDeletedEventAvro toDeletedEvent(Image image) {
    return ImageDeletedEventAvro.newBuilder()
        .setDate(new Date().getTime())
        .setIdentifier(image.getImageId().toString())
        .setUserIdentifier(image.getUserId().toString())
        .build();
  }
}
