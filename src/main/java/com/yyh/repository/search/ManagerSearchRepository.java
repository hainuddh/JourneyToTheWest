package com.yyh.repository.search;

import com.yyh.domain.Manager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Manager entity.
 */
public interface ManagerSearchRepository extends ElasticsearchRepository<Manager, Long> {
}
