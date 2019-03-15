package info.novatec.spring.showcase.comment.api;

import info.novatec.spring.showcase.comment.api.assembler.CommentAssembler;
import info.novatec.spring.showcase.comment.api.resource.assembler.CommentPageResourceAssembler;
import info.novatec.spring.showcase.comment.api.resource.assembler.CommentResourceAssembler;
import info.novatec.spring.showcase.comment.model.Comment;
import info.novatec.spring.showcase.comment.model.User;
import info.novatec.spring.showcase.comment.service.CommentService;
import info.novatec.spring.showcase.comment.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/images")
public class CommentController {

  @Autowired private UserService userService;

  @Autowired private CommentService commentService;

  @Autowired private CommentAssembler commentAssembler;

  @Autowired private CommentResourceAssembler commentResourceAssembler;

  @Autowired private CommentPageResourceAssembler commentPageResourceAssembler;

  @PostMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> addComment(@PathVariable("id") UUID imageId, @RequestParam String text) {
    Comment comment =
        commentService.save(commentAssembler.assemble(imageId, StringUtils.trimToNull(text)));
    List<Comment> comments = Collections.singletonList(comment);
    List<User> users = findUsers(comments);
    return ResponseEntity.ok(commentResourceAssembler.assemble(comment, users));
  }

  @GetMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> findByImageId(@PathVariable("id") UUID imageId, Pageable pageable) {
    Page<Comment> comments = commentService.findAllByImageIdentifier(imageId, pageable);
    List<User> users = findUsers(comments.getContent());
    return ResponseEntity.ok(commentPageResourceAssembler.assemble(comments, users));
  }

  private List<User> findUsers(Iterable<Comment> comments) {
    List<UUID> userIdentifiers =
        StreamSupport.stream(comments.spliterator(), false)
            .map(Comment::getUserIdentifier)
            .collect(Collectors.toList());
    if (userIdentifiers.isEmpty()) {
      return Collections.emptyList();
    }
    return userService.findAllByUserIdentifiers(userIdentifiers);
  }
}
