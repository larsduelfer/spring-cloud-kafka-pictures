package info.novatec.spring.showcase.search.service;

import info.novatec.spring.showcase.search.model.Image;
import info.novatec.spring.showcase.search.model.ModelConstants;
import info.novatec.spring.showcase.search.repository.ImageRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class ImageService {

  @Autowired private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired private ImageRepository imageRepository;

  @Autowired private ImageMessagingService imageMessagingService;

  /**
   * This method is to save (overwriting) an image without sending an event.
   *
   * @param image the image to save
   */
  public void create(Image image) {
    imageRepository.save(image);
  }

  /**
   * This method is to update the exif data of an image without sending an event.
   *
   * @param image the image to save
   */
  public void updateExifData(Image image) {
    Image existingImage = imageRepository.findOneByIdentifier(image.getIdentifier());
    existingImage.setExifData(image.getExifData());
    existingImage.setLastModifiedDate(image.getLastModifiedDate());
    imageRepository.save(existingImage);
  }

  /**
   * This method is to delete an image without sending an event
   *
   * @param imageId the id of the image to delete
   */
  public void deleteImage(UUID imageId) {
    imageRepository.deleteImage(imageId);
  }

  @Transactional
  @PreAuthorize("@imageAuthorizationComponent.hasWritePermission(#imageId)")
  public Image updateTitle(UUID imageId, String title) {
    Image image = imageRepository.findOneByIdentifier(imageId);
    image.setTitle(title);
    image.setLastModifiedDate(new Date());
    imageRepository.save(image);
    imageMessagingService.imageMetadataUpdated(image);
    return image;
  }

  public Image findOne(UUID identifier) {
    return imageRepository.findOneByIdentifier(identifier);
  }

  public Page<Image> findImages(String term, Pageable pageable) {
    SearchQuery findImagesByTerm =
        new NativeSearchQueryBuilder()
            .withQuery(matchAllQuery())
            .withIndices(ModelConstants.INDEX_IMAGE)
            .withTypes(ModelConstants.TYPE_IMAGE)
            .withPageable(pageable)
            .withFilter(
                boolQuery()
                    .should(
                        queryStringQuery("*" + term + "*")
                            .analyzeWildcard(true)
                            .field("title")
                            .field("exifData")))
            .withSort(SortBuilders.scoreSort().order(SortOrder.ASC))
            .build();

    return elasticsearchTemplate.queryForPage(findImagesByTerm, Image.class);
  }

  public Page<Image> findUserImages(UUID userIdentifier, Pageable pageable) {
    SearchQuery findImagesByUserId =
        new NativeSearchQueryBuilder()
            .withQuery(matchAllQuery())
            .withIndices(ModelConstants.INDEX_IMAGE)
            .withTypes(ModelConstants.TYPE_IMAGE)
            .withPageable(pageable)
            .withFilter(
                boolQuery()
                    .must(QueryBuilders.matchQuery("userIdentifier", userIdentifier.toString())))
            .withSort(SortBuilders.scoreSort().order(SortOrder.ASC))
            .build();

    return elasticsearchTemplate.queryForPage(findImagesByUserId, Image.class);
  }
}
