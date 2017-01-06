package com.yyh.repository.search;

import com.yyh.domain.CompanyType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanyType entity.
 */
public interface CompanyTypeSearchRepository extends ElasticsearchRepository<CompanyType, Long> {
}
