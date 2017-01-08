package com.yyh.repository.search;

import com.yyh.domain.TaskProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TaskProject entity.
 */
public interface TaskProjectSearchRepository extends ElasticsearchRepository<TaskProject, Long> {
}
