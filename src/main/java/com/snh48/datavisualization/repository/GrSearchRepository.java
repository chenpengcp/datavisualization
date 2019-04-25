package com.snh48.datavisualization.repository;

import com.snh48.datavisualization.pojo.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GrSearchRepository extends ElasticsearchRepository<User, Integer> {
}
