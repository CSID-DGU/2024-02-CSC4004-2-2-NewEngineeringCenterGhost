//package com.newengineeringghost.domain.api.repository;
//
//import com.newengineeringghost.domain.api.entity.ClassifyUrl;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.util.List;
//
//public interface ClassifyUrlRepository extends MongoRepository<ClassifyUrl,String> {
//    @Query("{ 'url' : { $regex: ?0 } }")
//    List<ClassifyUrl> findByUrlStartingWith(String url);
//}