package com.project.clientservice.service;

import com.project.clientservice.models.entity.ClientEntity;
import com.project.clientservice.models.request.CreateClientRequest;
import com.project.clientservice.models.request.UpdateClientRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Client service interface.
 */
public interface IClientService {

    // ############################### CRUD operations
    Mono<ClientEntity> create(CreateClientRequest clientDTO);
    Mono<ClientEntity> update(UpdateClientRequest clientDTO);
    Flux<ClientEntity> findAll();
    Mono<ClientEntity> findById(String id);
    Mono<ClientEntity> findByDocumentNumber(String documentNumber);
    Mono<ClientEntity> findByEmail(String email);
    Flux<ClientEntity> findByName(String name);
    Mono<ClientEntity> findByRuc(String ruc);
    // Logical on status
    Mono<ClientEntity> deleteClient(String id);
    // on database
    Mono<ClientEntity> removeById(String id);
    // on database
    Mono<Void> deleteAll();

    // ############################### EXTERNAL operations

    // ############################### USE CASE operations

}
