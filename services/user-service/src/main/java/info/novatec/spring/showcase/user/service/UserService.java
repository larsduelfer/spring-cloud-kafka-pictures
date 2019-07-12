package info.novatec.spring.showcase.user.service;

import info.novatec.spring.showcase.user.configuration.api.Version;
import info.novatec.spring.showcase.user.model.User;
import info.novatec.spring.showcase.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private UserMessagingService userMessagingService;

  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findOneByIdpId(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with id " + username + " not found."));
  }

  @Transactional(transactionManager = "chainedTransactionManager")
  public User createUser(User user) {
    User savedUser = userRepository.save(user);
    userMessagingService.userCreated(user);
    return savedUser;
  }

  @Transactional(transactionManager = "chainedTransactionManager")
  public User updateUser(
      User user, String displayName, String firstName, String lastName, Long version) {
    Version.of(version).verify(user);

    if (StringUtils.isBlank(displayName)
        || StringUtils.isBlank(firstName)
        || StringUtils.isBlank(lastName)) {
      throw new IllegalArgumentException(
          "First name, last name and display name are mandatory fields");
    }

    user.setDisplayName(displayName);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setLastModifiedDate(new Date());

    if (!user.isRegistered()) {
      user.setRegistered(true);
    }
    User savedUser = userRepository.save(user);
    userMessagingService.userUpdated(savedUser);
    return savedUser;
  }
}
