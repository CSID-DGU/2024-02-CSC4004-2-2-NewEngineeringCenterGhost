package com.newengineeringghost.domain.api.repository;

import com.newengineeringghost.domain.api.entity.ResponseData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataResponseRepository extends MongoRepository<ResponseData, String> {
}
