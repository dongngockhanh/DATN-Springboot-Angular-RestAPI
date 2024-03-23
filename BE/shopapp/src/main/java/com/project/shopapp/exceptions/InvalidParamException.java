package com.project.shopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//400
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParamException extends Exception {
    public InvalidParamException(String message)
    {
        super(message);
    }
}
