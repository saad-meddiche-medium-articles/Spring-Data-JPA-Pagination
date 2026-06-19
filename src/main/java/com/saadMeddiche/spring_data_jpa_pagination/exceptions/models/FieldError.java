package com.saadMeddiche.spring_data_jpa_pagination.exceptions.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;
}
