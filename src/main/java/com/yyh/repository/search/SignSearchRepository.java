package com.yyh.repository.search;

import com.yyh.domain.Sign;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Sign entity.
 */
public interface SignSearchRepository extends ElasticsearchRepository<Sign, Long> {
}
