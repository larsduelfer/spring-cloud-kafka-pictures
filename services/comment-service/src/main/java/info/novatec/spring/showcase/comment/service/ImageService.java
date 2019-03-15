package info.novatec.spring.showcase.comment.service;

import info.novatec.spring.showcase.comment.model.Image;
import info.novatec.spring.showcase.comment.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageService {

  @Autowired private ImageRepository imageRepository;

  @Autowired private CommentService commentService;

  public void create(Image image) {
    imageRepository.save(image);
  }

  public void deleteImage(UUID imageId) {
    commentService.deleteByImageIdentifier(imageId);
    imageRepository.deleteById(imageId);
  }

  Image findOneByIdentifier(UUID identifier) {
    return imageRepository.findOneByIdentifier(identifier);
  }
}
