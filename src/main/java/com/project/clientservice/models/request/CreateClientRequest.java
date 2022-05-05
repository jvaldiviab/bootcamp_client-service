package com.project.clientservice.models.request;

import com.project.clientservice.models.dto.Address;
import com.project.clientservice.models.dto.ClientType;
import com.project.clientservice.models.dto.EnterpriseInfo;
import com.project.clientservice.models.dto.PersonInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateClientRequest {
    private ClientType clientType;
    private PersonInfo personInfo;
    private EnterpriseInfo enterpriseInfo;
}
