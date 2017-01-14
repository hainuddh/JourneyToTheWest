package com.yyh.repository.search;

import com.yyh.domain.HeadPerson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HeadPerson entity.
 */
public interface HeadPersonSearchRepository extends ElasticsearchRepository<HeadPerson, Long> {
}
