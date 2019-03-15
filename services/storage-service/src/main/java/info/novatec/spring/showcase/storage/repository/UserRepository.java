package info.novatec.spring.showcase.storage.repository;

import info.novatec.spring.showcase.storage.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, BigInteger> {

  Optional<User> findOneByUserId(String userId);
}
