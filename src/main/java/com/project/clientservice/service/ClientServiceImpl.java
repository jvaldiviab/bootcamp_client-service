package com.project.clientservice.service;

import com.project.clientservice.models.dto.PersonInfo;
import com.project.clientservice.models.entity.ClientEntity;
import com.project.clientservice.models.request.CreateClientRequest;
import com.project.clientservice.models.request.UpdateClientRequest;
import com.project.clientservice.repository.IClientRepository;
import com.project.clientservice.utilities.util.IClientUtils;
import com.project.clientservice.utilities.constants.Constants;
import com.project.clientservice.utilities.errors.DuplicatedUniqueFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Client service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements  IClientService{

    private final IClientRepository clientRepository;
    private final IClientUtils clientsUtils;
    private final Constants constants;

    @Override
    public Mono<ClientEntity> create(CreateClientRequest clientDTO) {
        log.info(">> Starting the operation create client in service");
        log.info(">>>> Uniqueness check");


        ClientEntity currentClient = clientsUtils.clientCreateRequestToClient(clientDTO);
        Mono<ClientEntity> createdClient = findAll()
                .filter(retrievedClient -> clientsUtils.uniqueValuesDuplicity(currentClient, retrievedClient))
                .hasElements().flatMap(isARepeatedClient -> {
                    if (Boolean.TRUE.equals(isARepeatedClient)) {
                        log.warn(">>>>>> Client does not accomplish with uniqueness specifications");
                        log.error(">>>>>> Proceeding to abort create client operation");
                        return Mono.error(new DuplicatedUniqueFieldException("Client does not accomplish with uniqueness specifications"));
                    } else {
                        currentClient.setStatus(constants.getStatusActive());
                        currentClient.setCreateDate(new Date(System.currentTimeMillis()));
                        currentClient.setUpdateDate(new Date(System.currentTimeMillis()));
                        log.info(">>>>>> Creating new CLIENT: [{}]", currentClient.toString());
                        Mono<ClientEntity> newClient = clientRepository.insert(currentClient);
                        log.info(">>>>>> New customer was created successfully '{}'...", newClient.toString());
                        return newClient;
                    }
                });


        log.info(">> End of operation to create a client");
        return createdClient;
    }

    @Override
    public Mono<ClientEntity> update(UpdateClientRequest clientDTO) {
        log.info(">> Starting the operation update client in service");

        log.info(">>>> Validating customer existence and uniqueness");
        ClientEntity currentClient = clientsUtils.clientUpdateRequestToClient(clientDTO);
        Mono<ClientEntity> updatedClient = findById(currentClient.getId())
                .hasElement().flux().flatMap(customerExists -> {
                    if (Boolean.TRUE.equals(customerExists)) {
                        log.info(">>>>>> Client with id [{}] exists in database", currentClient.getId());
                        log.info(">>>>>> Proceeding to validate customer uniqueness");
                        return findAll();
                    } else {
                        log.warn(">>>>>> Client with id [{}] does not exist in database", currentClient.getId());
                        log.warn(">>>>>> Proceeding to abort update operation");
                        return Flux.error(new NoSuchElementException("Client does not exist in database"));
                    }
                })
                .filter(retrievedClient -> clientsUtils.uniqueValuesDuplicity(currentClient, retrievedClient))
                .hasElements().flatMap(isARepeatedClient -> {
                    if (Boolean.TRUE.equals(isARepeatedClient)) {
                        log.warn(">>>>>> Client does not accomplish with uniqueness specifications");
                        log.warn(">>>>>> Proceeding to abort update operation");
                        return Mono.error(new DuplicatedUniqueFieldException("Client does not accomplish with uniqueness specifications"));
                    }
                    else {
                        log.info(">>>>>> Updating client: [{}]", currentClient.toString());
                        Mono<ClientEntity> newClient = clientRepository.save(currentClient);
                        log.info(">>>>>> Client with id: [{}] was successfully updated", currentClient.getId());
                        return newClient;
                    }
                });

        log.info(">> End of operation to update a customer");
        return updatedClient;
    }

    @Override
    public Flux<ClientEntity> findAll() {
        log.info(">> Start of operation to retrieve all clients");
        log.info(">>>> Retrieving all clients");
        Flux<ClientEntity> retrievedCustomers = clientRepository.findAll();
        log.info(">>>> All clients retrieved successfully");
        log.info(">> End of operation to retrieve all clients");
        return retrievedCustomers;
    }

    @Override
    public Mono<ClientEntity> findById(String id) {
        log.info(">> Start of operation to find a client by id");
        log.info(">>>> Retrieving client with id: [{}]", id);
        Mono<ClientEntity> retrievedClient = clientRepository.findById(id);
        log.info(">>>> Client with id: [{}] was retrieved successfully", id);
        log.info(">> End of operation to find a client by id");
        return retrievedClient;
    }

    @Override
    public Mono<ClientEntity> findByDocumentNumber(String documentNumber) {
        log.info(">> Start of operation to find a client by id");
        log.info(">>>> Retrieving client with documentNumber: [{}]", documentNumber);
        Mono<ClientEntity> retrievedClient = clientRepository.findAll()
                .filter(client -> client.getPersonInfo().getDocumentNumber().equals(documentNumber)).take(1).single();
        log.info(">>>> Client with documentNumber: [{}] was retrieved successfully", documentNumber);
        log.info(">> End of operation to find a client by documentNumber");
        return retrievedClient;
    }

    @Override
    public Mono<ClientEntity> findByEmail(String email) {
        log.info(">> Start of operation to find a client by email");
        log.info(">>>> Retrieving client with email: [{}]", email);
        Mono<ClientEntity> retrievedClient = clientRepository.findAll()
                .filter(client -> client.getPersonInfo().getEmail().equals(email)).take(1).single();
        log.info(">>>> Client with email: [{}] was retrieved successfully", email);
        log.info(">> End of operation to find a client by email");
        return retrievedClient;
    }

    @Override
    public Flux<ClientEntity> findByName(String name) {
        log.info(">> Start of operation to find a client by name");
        log.info(">>>> Retrieving client with name: [{}]", name);
        Flux<ClientEntity> retrievedClient = clientRepository.findAll()
                .filter(client -> client.getPersonInfo().getName().equals(name));
        log.info(">>>> Client with name: [{}] was retrieved successfully", name);
        log.info(">> End of operation to find a client by name");
        return retrievedClient;
    }

    @Override
    public Mono<ClientEntity> findByRuc(String ruc) {
        log.info(">> Start of operation to find a client by ruc");
        log.info(">>>> Retrieving client with ruc: [{}]", ruc);
        Mono<ClientEntity> retrievedClient = clientRepository.findAll()
                .filter(clientEntity -> clientEntity.getClientType().getDescription().equals(constants.getClientEnterprise().toString()))
                .filter(client -> client.getEnterpriseInfo().getRuc().equals(ruc)).take(1).single();
        log.info(">>>> Client with ruc: [{}] was retrieved successfully", ruc);
        log.info(">> End of operation to find a client by ruc");
        return retrievedClient;
    }

    @Override
    public Mono<ClientEntity> removeById(String id) {
        log.info(">> Starting of operation to remove a client");
        log.info(">>>> Deleting client with id: [{}]", id);
        Mono<ClientEntity> removedClient = findById(id)
                .flatMap(retrievedClient -> clientRepository.deleteById(retrievedClient.getId()).thenReturn(retrievedClient));
        log.info(">>>> Client with id: [{}] was successfully deleted", id);
        log.info(">> End of operation to remove a client");
        return removedClient;
    }

    @Override
    public Mono<ClientEntity> deleteClient(String id) {
        log.info(">> Starting of operation to remove a client");
        log.info(">>>> Deleting client with id: [{}]", id);
        Mono<ClientEntity> removedClient = findById(id)
                .map(retrievedClient -> {
                    retrievedClient.setStatus(constants.getStatusBlocked());
                    clientRepository.save(retrievedClient).subscribe();
                    return retrievedClient;
                });
        log.info(">>>> Client with id: [{}] was blocked", id);
        log.info(">> End of operation to remove a client");
        return removedClient;
    }

    @Override
    public Mono<Void> deleteAll() {
        log.info(">> Starting of operation to remove all clients");
        Mono<Void> operationDelete = clientRepository.deleteAll();
        log.info(">> End of operation to remove all clients");
        return operationDelete;
    }

}
