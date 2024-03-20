package com.project.shopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class InvalidParamException extends Exception {
    public InvalidParamException(String message)
    {
        super(message);
    }
}
