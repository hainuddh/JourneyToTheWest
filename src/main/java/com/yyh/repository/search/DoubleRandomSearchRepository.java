package com.yyh.repository.search;

import com.yyh.domain.DoubleRandom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DoubleRandom entity.
 */
public interface DoubleRandomSearchRepository extends ElasticsearchRepository<DoubleRandom, Long> {
}
