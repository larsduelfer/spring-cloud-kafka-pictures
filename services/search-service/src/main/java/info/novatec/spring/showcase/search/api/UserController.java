package info.novatec.spring.showcase.search.api;

import info.novatec.spring.showcase.search.api.resource.assembler.UserPageResourceAssembler;
import info.novatec.spring.showcase.search.api.resource.assembler.UserResourceAssembler;
import info.novatec.spring.showcase.search.model.User;
import info.novatec.spring.showcase.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;

  @Autowired private UserPageResourceAssembler userPageResourceAssembler;

  @Autowired private UserResourceAssembler userResourceAssembler;

  @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> search(@RequestParam(value = "term") String term, Pageable pageable) {
    Page<User> users = userService.findUsers(term, pageable);
    return ResponseEntity.ok(userPageResourceAssembler.assemble(users));
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> findOne(@PathVariable("id") UUID identifier) {
    User user = userService.findOne(identifier);
    if (user == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(userResourceAssembler.assemble(Optional.of(user)));
  }
}
