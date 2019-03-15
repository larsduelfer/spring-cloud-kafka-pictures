package info.novatec.spring.showcase.comment.repository;

import info.novatec.spring.showcase.comment.model.Comment;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface CommentRepository
    extends CassandraRepository<Comment, UUID> {

  Slice<Comment> findAllByImageIdentifier(UUID imageIdentifier, Pageable pageable);

  void deleteByImageIdentifier(UUID imageIdentifier);
}
