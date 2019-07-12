package info.novatec.spring.showcase.user.repository;

import info.novatec.spring.showcase.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByIdpId(String idpId);
}
