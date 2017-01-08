package com.yyh.repository.search;

import com.yyh.domain.Law;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Law entity.
 */
public interface LawSearchRepository extends ElasticsearchRepository<Law, Long> {
}
