package info.novatec.spring.showcase.comment.repository;

import info.novatec.spring.showcase.comment.model.Image;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface ImageRepository extends CassandraRepository<Image, UUID> {

  Image findOneByIdentifier(UUID identifier);
}
