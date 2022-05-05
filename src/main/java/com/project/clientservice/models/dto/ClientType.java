package com.project.clientservice.models.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientType {
    @NotBlank(message = "Field description must be required(Personal or Enterprise)")
    private String description; // personal OR enterprise not inclusive
    @NotBlank(message = "Field type must be required")
    private String type; // others
}