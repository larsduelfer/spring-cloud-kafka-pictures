package info.novatec.spring.showcase.search.repository;

import info.novatec.spring.showcase.search.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends ElasticsearchRepository<User, UUID> {

  Optional<User> findOneByIdpId(String idpId);

  User findOneByIdentifier(UUID identifier);

  List<User> findAllByIdentifierIn(List<UUID> userIdentifiers);
}
