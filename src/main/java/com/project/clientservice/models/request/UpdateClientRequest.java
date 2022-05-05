package com.project.clientservice.models.request;

import com.project.clientservice.models.dto.Address;
import com.project.clientservice.models.dto.ClientType;
import com.project.clientservice.models.dto.EnterpriseInfo;
import com.project.clientservice.models.dto.PersonInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateClientRequest {
    private String id;
    private ClientType clientType;
    private String status;
    private PersonInfo personInfo;
    private EnterpriseInfo enterpriseInfo;
}

