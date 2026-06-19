package com.saadMeddiche.spring_data_jpa_pagination.exceptions.customs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

    private final HttpStatus status;

    private final String message;

}