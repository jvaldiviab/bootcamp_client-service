package com.project.clientservice.utilities.util;

import com.project.clientservice.models.entity.ClientEntity;
import com.project.clientservice.models.request.CreateClientRequest;
import com.project.clientservice.models.request.UpdateClientRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientUtils {
    Boolean uniqueValuesDuplicity(ClientEntity clientA, ClientEntity clientB);
    ClientEntity clientCreateRequestToClient(CreateClientRequest createClientReq);
    ClientEntity clientUpdateRequestToClient(UpdateClientRequest updateClientReq);
    CreateClientRequest clientToClientCreateRequest(ClientEntity client);
    UpdateClientRequest clientToClientUpdateRequest(ClientEntity client);

}