package com.project.clientservice.models.entity;

import com.project.clientservice.models.dto.Address;
import com.project.clientservice.models.dto.ClientType;
import com.project.clientservice.models.dto.EnterpriseInfo;
import com.project.clientservice.models.dto.PersonInfo;
import com.project.clientservice.models.request.CreateClientRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * Client object.
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "Clients")
public class ClientEntity {
    @Id
    private String id;
    private ClientType clientType; // Personal or enterprise
    private String status; // active or inactive
    private PersonInfo personInfo; // information personal
    private EnterpriseInfo enterpriseInfo; // information enterprise
    private Date createDate; // creation date
    private Date updateDate; // update date
    private Date lastOperationUpdate; // last operation done

}
