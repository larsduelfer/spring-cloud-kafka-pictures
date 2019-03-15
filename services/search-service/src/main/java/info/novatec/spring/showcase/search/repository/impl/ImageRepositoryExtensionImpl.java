package info.novatec.spring.showcase.search.repository.impl;

import info.novatec.spring.showcase.search.model.Image;
import info.novatec.spring.showcase.search.model.ModelConstants;
import info.novatec.spring.showcase.search.repository.ImageRepositoryExtension;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;

import java.util.UUID;

public class ImageRepositoryExtensionImpl implements ImageRepositoryExtension {

  @Autowired private ElasticsearchTemplate elasticsearchTemplate;

  @Override
  public void deleteImage(UUID imageId) {
    DeleteQuery deleteQuery = new DeleteQuery();
    deleteQuery.setQuery(QueryBuilders.termQuery("identifier", imageId.toString()));
    deleteQuery.setIndex(ModelConstants.INDEX_IMAGE);
    deleteQuery.setType(ModelConstants.TYPE_IMAGE);
    elasticsearchTemplate.delete(deleteQuery, Image.class);
  }
}
