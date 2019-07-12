package info.novatec.spring.showcase.search.service;

import info.novatec.spring.showcase.search.model.ModelConstants;
import info.novatec.spring.showcase.search.model.User;
import info.novatec.spring.showcase.search.repository.UserRepository;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private ElasticsearchTemplate elasticsearchTemplate;

  public User loadUserByUsername(String username) {
    return userRepository
        .findOneByIdpId(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with id " + username + " not found."));
  }

  public List<User> findAllByUserIdentifiers(List<UUID> userIdentifiers) {
    return userRepository.findAllByIdentifierIn(userIdentifiers);
  }

  public User findOne(UUID identifier) {
    return userRepository.findOneByIdentifier(identifier);
  }

  public Page<User> findUsers(String term, Pageable pageable) {
    SearchQuery findUserQuery =
        new NativeSearchQueryBuilder()
            .withQuery(matchAllQuery())
            .withIndices(ModelConstants.INDEX_USER)
            .withTypes(ModelConstants.TYPE_USER)
            .withPageable(pageable)
            .withFilter(
                boolQuery()
                    .should(
                        queryStringQuery("*" + term + "*")
                            .analyzeWildcard(true)
                            .field("displayName")))
            .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
            .build();

    return elasticsearchTemplate.queryForPage(findUserQuery, User.class);
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
