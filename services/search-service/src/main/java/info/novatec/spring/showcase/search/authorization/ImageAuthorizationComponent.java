package info.novatec.spring.showcase.search.authorization;

import info.novatec.spring.showcase.search.configuration.security.SecurityContextHelper;
import info.novatec.spring.showcase.search.model.Image;
import info.novatec.spring.showcase.search.model.User;
import info.novatec.spring.showcase.search.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageAuthorizationComponent {

  @Autowired private ImageRepository imageRepository;

  public boolean hasWritePermission(UUID imageId) {
    User currentUser = SecurityContextHelper.getInstance().getCurrentUser();

    Image image = imageRepository.findOneByIdentifier(imageId);
    if (image == null) {
      return false;
    }
    return currentUser.getIdentifier().equals(image.getUserIdentifier());
  }
}
