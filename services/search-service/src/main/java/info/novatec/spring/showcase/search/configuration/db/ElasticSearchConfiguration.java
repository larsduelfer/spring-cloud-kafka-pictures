package info.novatec.spring.showcase.search.configuration.db;

import info.novatec.spring.showcase.search.SearchServiceApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = SearchServiceApplication.class)
public class ElasticSearchConfiguration {}
