package com.yyh.repository.search;

import com.yyh.domain.DoubleRandomResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DoubleRandomResult entity.
 */
public interface DoubleRandomResultSearchRepository extends ElasticsearchRepository<DoubleRandomResult, Long> {
}
