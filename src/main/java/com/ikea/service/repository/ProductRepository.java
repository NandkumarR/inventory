package com.ikea.service.repository;

import com.ikea.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nandk
 * Mongo Repositry for CRUD operations for Products
 */
@Repository
public interface ProductRepository extends MongoRepository<Product,String> {
}
