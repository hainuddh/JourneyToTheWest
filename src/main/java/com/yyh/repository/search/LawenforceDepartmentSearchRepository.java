package com.yyh.repository.search;

import com.yyh.domain.LawenforceDepartment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LawenforceDepartment entity.
 */
public interface LawenforceDepartmentSearchRepository extends ElasticsearchRepository<LawenforceDepartment, Long> {
}
