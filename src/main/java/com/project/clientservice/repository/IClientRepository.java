package com.project.clientservice.repository;

import com.project.clientservice.models.entity.ClientEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Client repository.
 */
@Repository
public interface IClientRepository extends ReactiveMongoRepository<ClientEntity, String> {
}
