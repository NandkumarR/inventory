package com.ikea.service.repository;

import com.ikea.model.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author nandk .
 * Mongo Repositry for CRUD operations for Articles
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article,String> {
    List<Article> findByIdentificationNumberIn(List<Integer> identificationNumber);
    Article findByIdentificationNumber(Integer indentificationNumber);
}
