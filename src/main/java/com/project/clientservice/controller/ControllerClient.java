package com.project.clientservice.controller;

import com.project.clientservice.models.entity.ClientEntity;
import com.project.clientservice.models.request.CreateClientRequest;
import com.project.clientservice.models.request.UpdateClientRequest;
import com.project.clientservice.service.IClientService;
import com.project.clientservice.utilities.constants.Constants;
import com.project.clientservice.utilities.errors.DuplicatedUniqueFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.status.StatusLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ControllerClient {

    @Autowired
    IClientService clientService;
    @Autowired
    Constants constants;

    Logger LOGGER = StatusLogger.getLogger();

    @RequestMapping("/")
    public String home(){
        return "Client-Service running at port: " + constants.getClientPort();
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<ClientEntity>> createClient(@RequestBody CreateClientRequest clientDTO) {
        LOGGER.info("Try creating client '{}'...", clientDTO.toString());
        LOGGER.info("Post operation in /create");

        return clientService.create(clientDTO)
                .flatMap(createdClient -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdClient)))
                .onErrorResume(DuplicatedUniqueFieldException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ClientEntity>> updateClient(@RequestBody UpdateClientRequest clientDTO) {
        LOGGER.info("Try updating client '{}'...", clientDTO.toString());
        LOGGER.info("Put operation in /update");
        return clientService.update(clientDTO)
                .flatMap(updatedClient -> Mono.just(ResponseEntity.ok(updatedClient)))
                .onErrorResume(DuplicatedUniqueFieldException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/findAll")
    public Flux<ClientEntity> getAllClients(){
        LOGGER.info("Try find all clients");
        LOGGER.info("Get operation in /findAll");
        return clientService.findAll();
    }

    @GetMapping("/getById/{id}")
    public Mono<ResponseEntity<ClientEntity>> getClientById(@PathVariable(value = "id") String clientId) {
        LOGGER.info("Try get client by ID  '{}'...", clientId);
        LOGGER.info("Get operation in /getById/"+ clientId);
        return clientService.findById(clientId)
                .map(saveClient -> ResponseEntity.ok(saveClient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/getByDocumentNumber/{doc}")
    public Mono<ResponseEntity<ClientEntity>> getClientByDocumentNumber(@PathVariable(value = "doc") String documentNumber) {
        LOGGER.info("Try get client by DocumentNumber  '{}'...", documentNumber);
        LOGGER.info("Get operation in /getByDocumentNumber/"+ documentNumber);
        return clientService.findByDocumentNumber(documentNumber)
                .map(saveClient -> ResponseEntity.ok(saveClient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/getByEmail/{email}")
    public Mono<ResponseEntity<ClientEntity>> getClientByEmail(@PathVariable(value = "email") String email) {
        LOGGER.info("Try get client by email  '{}'...", email);
        LOGGER.info("Get operation in /getByEmail/"+ email);
        return clientService.findByEmail(email)
                .map(saveClient -> ResponseEntity.ok(saveClient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/getByName/{name}")
    public Flux<ClientEntity> getClientByName(@PathVariable(value = "name") String name) {
        LOGGER.info("Try get client by name  '{}'...", name);
        LOGGER.info("Get operation in /getByName/"+ name);
        return clientService.findByName(name);
    }

    @GetMapping("/getByRuc/{ruc}")
    public Mono<ResponseEntity<ClientEntity>> getClientByRuc(@PathVariable(value = "ruc") String ruc) {
        LOGGER.info("Try get client by ruc  '{}'...", ruc);
        LOGGER.info("Get operation in /getByRuc/"+ ruc);
        return clientService.findByRuc(ruc)
                .map(saveClient -> ResponseEntity.ok(saveClient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Delete client by id.
     */
    @DeleteMapping("/removeById/{id}")
    public Mono<ClientEntity> deleteClient(@PathVariable("id") String clientId) {
        LOGGER.info("Try delete client by ID  '{}'...", clientId);
        LOGGER.info("Delete: operation in /removeById/"+ clientId);
        return clientService.removeById(clientId);
    }

    @DeleteMapping("/deleteClient/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable(value = "id") String clientId) {
        LOGGER.info("Try delete client by ID  '{}'...", clientId);
        LOGGER.info("Delete: operation in /deleteClient/"+ clientId);

        return clientService.deleteClient(clientId)  // First, you search the Bucket you want to delete.
                .flatMap(existingClient ->Mono.just(new ResponseEntity<Void>(HttpStatus.OK))                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));  // or you return 404 NOT FOUND to say the Bucket was not found
    }

    @DeleteMapping("/deleteAllClients")
    public Mono<Void> deleteAllClients(){
        LOGGER.info("Try delete all clients");
        LOGGER.info("Delete: operation in /deleteAllClients/");

        return clientService.deleteAll();
    }

    /*@GetMapping("/getByDocument/{documentNumber}")
    public Mono<ResponseEntity<ClientEntity>> getClientByDocument(@PathVariable(value = "documentNumber") String documentNumber) {
        return clientService.findByDocument(documentNumber)
                .map(saveClient -> ResponseEntity.ok(saveClient))  // then the map operator is called on this Bucket to wrap it in a ResponseEntity object with status code 200 OK
                .defaultIfEmpty(ResponseEntity.notFound().build());   // finally there is a call to defaultIfEmpty to build an empty ResponseEntity with status 404 NOT FOUND if the Bucket was not found.
    }*/



    /* @PutMapping("/update/{id}")
    public Mono<ResponseEntity<ClientEntity>> update(@PathVariable(value = "id") String clientId,
                                                           @Valid @RequestBody ClientEntity client){
        return clientService.findById(clientId)
                .flatMap(existingClient -> {
                    existingClient.setDocumentNumber(client.getDocumentNumber());
                    existingClient.setFirstName(client.getFirstName());
                    existingClient.setLastName(client.getLastName());
                    existingClient.setType(client.getType());
                    existingClient.setActive(client.isActive());// then calls flatMap with this movie to update its entries using its setters and the values from the Bucket passed as argument.
                    return clientService.saveClient(existingClient);
                })
                .map(updatedClient -> new ResponseEntity<>(updatedClient, HttpStatus.OK))  // Then it saves them to the database and wraps this updated Bucket in a ResponseEntity with status code 200 OK in case of success or 404 NOT FOUND in case of failure.
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/



    /*@GetMapping("/test")
    public Flux<SaveAccountResponse> test(){
        Flux<SaveAccountResponse> assetsFlux = WebClient.create("http://localhost:8002/api/getAll")
                .get().retrieve().bodyToFlux(SaveAccountResponse.class);
        return assetsFlux;
    }

    @GetMapping("/liabilityByUser/{clientId}")
    public Flux<SaveAccountResponse> liabilityByUser(@PathVariable("clientId") String clientId){
        Flux<SaveAccountResponse> liabilityFlux = WebClient.create("http://localhost:8003/api/getLiabilityByUserId/"+clientId)
                .get().retrieve().bodyToFlux(SaveAccountResponse.class);
        return liabilityFlux;

    }

    @PostMapping("/createSaveAccount/{clientId}")
    public Mono<SaveAccountResponse> saveLiability(@PathVariable("clientId") String clientId,@Valid @RequestBody SaveAccountResponse saveAccountResponse){
        return WebClient.create("http://localhost:8003/api/create")
                .post()
                .body((Object) Mono.just(saveAccountResponse).flatMap(existingAR -> {
                    existingAR.setUserId(clientId);
                    return Mono.just(existingAR);
                }), SaveAccountRequest.class)
                .retrieve()
                .bodyToMono(SaveAccountResponse.class);
    }*/



}
