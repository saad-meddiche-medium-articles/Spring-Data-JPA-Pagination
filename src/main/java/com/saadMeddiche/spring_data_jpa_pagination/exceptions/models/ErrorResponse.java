package com.saadMeddiche.spring_data_jpa_pagination.exceptions.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;

    private String error;

    private String message;

    private List<FieldError> fieldErrors;

}
