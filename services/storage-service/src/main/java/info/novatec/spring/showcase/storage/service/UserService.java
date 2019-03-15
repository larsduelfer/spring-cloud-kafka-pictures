package info.novatec.spring.showcase.storage.service;

import info.novatec.spring.showcase.storage.model.User;
import info.novatec.spring.showcase.storage.repository.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private MongoOperations mongoOperations;

  @Transactional(readOnly = true)
  public User loadUserByUsername(String username) {
    return userRepository
        .findOneByUserId(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with id " + username + " not found."));
  }

  @Transactional
  public void upsert(User user) {
    mongoOperations.upsert(
        new Query(Criteria.where("identifier").is(user.getIdentifier())),
        toUpdate(user),
        User.class);
  }

  private Update toUpdate(User user) {
    Document document = new Document();
    mongoOperations.getConverter().write(user, document);
    Update update = new Update();
    for (Map.Entry<String, Object> entry : document.entrySet()) {
      update.set(entry.getKey(), entry.getValue());
    }
    return update;
  }
}
