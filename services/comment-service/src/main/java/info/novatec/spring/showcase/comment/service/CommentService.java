package info.novatec.spring.showcase.comment.service;

import info.novatec.spring.showcase.comment.model.Comment;
import info.novatec.spring.showcase.comment.model.Image;
import info.novatec.spring.showcase.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CommentService {

  @Autowired private CommentRepository commentRepository;

  @Autowired private CommentMessagingService commentMessagingService;

  private ImageService imageService;

  @Transactional
  public Comment save(Comment comment) {
    Image image = imageService.findOneByIdentifier(comment.getImageIdentifier());
    if (image == null) {
      throw new IllegalArgumentException("Image not found");
    }
    Comment savedComment = commentRepository.save(comment);
    commentMessagingService.commentAdded(savedComment);
    return savedComment;
  }

  public Page<Comment> findAllByImageIdentifier(UUID imageIdentifier, Pageable pageable) {
    Slice<Comment> comments = commentRepository.findAllByImageIdentifier(imageIdentifier, pageable);
    return new PageImpl<>(
        comments.getContent(), comments.getPageable(), comments.getNumberOfElements());
  }

  void deleteByImageIdentifier(UUID imageIdentifier) {
    commentRepository.deleteByImageIdentifier(imageIdentifier);
  }

  @Autowired
  public void setImageService(ImageService imageService) {
    this.imageService = imageService;
  }
}
