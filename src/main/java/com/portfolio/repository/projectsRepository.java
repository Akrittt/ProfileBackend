package com.portfolio.repository;

import com.portfolio.entity.projectEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface projectsRepository extends MongoRepository<projectEntity,String> {
}
