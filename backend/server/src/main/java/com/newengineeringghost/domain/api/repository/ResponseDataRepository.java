package com.newengineeringghost.domain.api.repository;

import com.newengineeringghost.domain.api.entity.ResponseData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponseDataRepository extends MongoRepository<ResponseData,String> {
    Optional<ResponseData> findResponseDataByLink(String link);
}