package info.novatec.spring.showcase.storage.service.assembler;

import info.novatec.spring.showcase.image.message.v1.resource.ImageDeletedEvent;
import info.novatec.spring.showcase.image.message.v1.resource.ImageUploadedEvent;
import info.novatec.spring.showcase.model.Image;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ImageEventAssembler {

  public ImageUploadedEvent toUploadedEvent(Image image, String title) {
    return new ImageUploadedEvent(image.getCreatedDate(), image.getImageId(), image.getUserId(), title);
  }

  public ImageDeletedEvent toDeletedEvent(Image image) {
    return new ImageDeletedEvent(new Date(), image.getImageId(), image.getUserId());
  }
}
