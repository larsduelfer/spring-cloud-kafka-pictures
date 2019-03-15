package info.novatec.spring.showcase.search.repository;

import info.novatec.spring.showcase.search.model.Image;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ImageRepository
    extends ElasticsearchRepository<Image, UUID>, ImageRepositoryExtension {

  Image findOneByIdentifier(UUID identifier);
}
