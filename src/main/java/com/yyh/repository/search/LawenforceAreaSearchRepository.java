package com.yyh.repository.search;

import com.yyh.domain.LawenforceArea;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LawenforceArea entity.
 */
public interface LawenforceAreaSearchRepository extends ElasticsearchRepository<LawenforceArea, Long> {
}
