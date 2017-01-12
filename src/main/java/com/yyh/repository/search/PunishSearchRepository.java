package com.yyh.repository.search;

import com.yyh.domain.Punish;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Punish entity.
 */
public interface PunishSearchRepository extends ElasticsearchRepository<Punish, Long> {
}
