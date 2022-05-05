package com.project.clientservice.utilities.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.port}")
    private String clientPort;

    @Value("${constants.client.personal.description}")
    private String clientPersonal;
    @Value("${constants.client.personal.type.normal}")
    private String clientPersonalNormal;
    @Value("${constants.client.personal.type.vip}")
    private String clientPersonalVip;

    @Value("${constants.client.enterprise.description}")
    private String clientEnterprise;
    @Value("${constants.client.enterprise.type.normal}")
    private String clientEnterpriseNormal;
    @Value("${constants.client.enterprise.type.pyme}")
    private String clientEnterprisePyme;

    @Value("${constants.status.blocked}")
    private String statusBlocked;
    @Value("${constants.status.active}")
    private String statusActive;

    @Value("${constants.services.prefix}")
    private String servicesPrefix;
    @Value("${constants.services.url.gateway}")
    private String servicesUrlGateway;
    @Value("${constants.services.url.client}")
    private String servicesUrlClient;
    @Value("${constants.services.url.passive}")
    private String servicesUrlPassive;
    @Value("${constants.services.url.active}")
    private String servicesUrlActive;
    @Value("${constants.services.path.client}")
    private String servicesPathClient;
    @Value("${constants.services.path.passive}")
    private String servicesPathPassive;
    @Value("${constants.services.path.active}")
    private String servicesPathActive;



}