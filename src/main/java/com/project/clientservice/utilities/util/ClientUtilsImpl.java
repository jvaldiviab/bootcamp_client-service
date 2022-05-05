package com.project.clientservice.utilities.util;

import com.project.clientservice.models.entity.ClientEntity;
import com.project.clientservice.models.request.CreateClientRequest;
import com.project.clientservice.models.request.UpdateClientRequest;
import com.project.clientservice.utilities.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientUtilsImpl implements IClientUtils {

    private final Constants constants;

    @Override
    public Boolean uniqueValuesDuplicity(ClientEntity clientA, ClientEntity clientB) {

        if (clientA == null )
            return true;
        // A customer can not be duplicated by its own
        if (clientA.getId() != null && clientB.getId() != null && clientA.getId().contentEquals(clientB.getId()))
            return false;
        if (clientA.getClientType().getDescription().contentEquals(constants.getClientPersonal().toString()) &&
                clientB.getPersonInfo().getDocumentNumber() != null) {
            // Unique value for Personal Accounts is Identity Number
            return clientA.getPersonInfo().getDocumentNumber().contentEquals(clientB.getPersonInfo().getDocumentNumber());
        } else if (clientA.getClientType().getDescription().contentEquals(constants.getClientEnterprise().toString())) {
            if (clientB.getEnterpriseInfo().getRuc() == null)
                return false;
            return clientA.getEnterpriseInfo().getRuc().contentEquals(clientB.getEnterpriseInfo().getRuc()) ||
                    clientA.getEnterpriseInfo().getName().contentEquals(clientB.getEnterpriseInfo().getName().toString());
        } else return false;
    }

    @Override
    public ClientEntity clientCreateRequestToClient(CreateClientRequest clientDTO) {
        return ClientEntity.builder()
                .clientType(clientDTO.getClientType())
                .personInfo(clientDTO.getPersonInfo())
                .enterpriseInfo(clientDTO.getEnterpriseInfo())
                .build();
    }

    @Override
    public ClientEntity clientUpdateRequestToClient(UpdateClientRequest clientDTO) {
        return ClientEntity.builder()
                .id(clientDTO.getId())
                .status(clientDTO.getStatus())
                .clientType(clientDTO.getClientType())
                .personInfo(clientDTO.getPersonInfo())
                .enterpriseInfo(clientDTO.getEnterpriseInfo())
                .updateDate(new Date(System.currentTimeMillis()))
                .build();
    }

    @Override
    public CreateClientRequest clientToClientCreateRequest(ClientEntity client) {
        return CreateClientRequest.builder()
                .clientType(client.getClientType())
                .personInfo(client.getPersonInfo())
                .enterpriseInfo(client.getEnterpriseInfo())
                .build();
    }

    @Override
    public UpdateClientRequest clientToClientUpdateRequest(ClientEntity client) {
        return UpdateClientRequest.builder()
                .id(client.getId())
                .status(client.getStatus())
                .clientType(client.getClientType())
                .personInfo(client.getPersonInfo())
                .enterpriseInfo(client.getEnterpriseInfo())
                .build();
    }
}