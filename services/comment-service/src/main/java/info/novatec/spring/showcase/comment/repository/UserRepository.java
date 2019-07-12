package info.novatec.spring.showcase.comment.repository;

import info.novatec.spring.showcase.comment.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CassandraRepository<User, UUID> {

  Optional<User> findOneByIdpId(String idpId);

  List<User> findAllByIdentifierIn(List<UUID> userIdentifiers);
}
