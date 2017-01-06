package com.yyh.repository.search;

import com.yyh.domain.IndustryType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the IndustryType entity.
 */
public interface IndustryTypeSearchRepository extends ElasticsearchRepository<IndustryType, Long> {
}
