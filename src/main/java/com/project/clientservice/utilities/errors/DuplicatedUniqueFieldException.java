package com.project.clientservice.utilities.errors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DuplicatedUniqueFieldException extends IllegalArgumentException {
    public DuplicatedUniqueFieldException(String s) {
        super(s);
    }
}