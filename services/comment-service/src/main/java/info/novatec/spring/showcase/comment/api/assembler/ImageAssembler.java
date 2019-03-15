package info.novatec.spring.showcase.comment.api.assembler;

import info.novatec.spring.showcase.comment.model.Image;
import info.novatec.spring.showcase.image.message.v1.resource.ImageUploadedEvent;
import org.springframework.stereotype.Component;

@Component
public class ImageAssembler {

  public Image assemble(ImageUploadedEvent imageUploadedEvent) {
    return new Image(imageUploadedEvent.getIdentifier(), imageUploadedEvent.getUserIdentifier());
  }
}
