package info.novatec.spring.showcase.comment.api.assembler;

import info.novatec.spring.showcase.comment.model.Image;
import info.novatec.spring.showcase.image.message.ImageUploadedEventAvro;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageAssembler {

  public Image assemble(ImageUploadedEventAvro eventAvro) {
    return new Image(
        UUID.fromString(eventAvro.getIdentifier()), UUID.fromString(eventAvro.getUserIdentifier()));
  }
}
