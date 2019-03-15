package info.novatec.spring.showcase.comment.service;

import info.novatec.spring.showcase.comment.model.User;
import info.novatec.spring.showcase.comment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  public User loadUserByUsername(String username) {
    return userRepository
        .findOneByUserId(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with id " + username + " not found."));
  }

  public List<User> findAllByUserIdentifiers(List<UUID> userIdentifiers) {
    return userRepository.findAllByIdentifierIn(userIdentifiers);
  }

  public User update(User user) {
    Optional<User> existingUser = userRepository.findById(user.getIdentifier());
    if (existingUser.isPresent()) {
      User usr = existingUser.get();
      usr.setDisplayName(user.getDisplayName());
      return userRepository.save(usr);
    } else {
      throw new IllegalArgumentException("User not found");
    }
  }

  public User save(User user) {
    return userRepository.save(user);
  }
}
