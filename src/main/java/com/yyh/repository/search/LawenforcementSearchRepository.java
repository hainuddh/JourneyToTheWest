package com.yyh.repository.search;

import com.yyh.domain.Lawenforcement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lawenforcement entity.
 */
public interface LawenforcementSearchRepository extends ElasticsearchRepository<Lawenforcement, Long> {
}
