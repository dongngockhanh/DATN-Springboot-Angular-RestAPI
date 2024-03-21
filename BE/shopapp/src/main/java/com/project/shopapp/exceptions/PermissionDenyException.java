package com.project.shopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PermissionDenyException extends RuntimeException {
    public PermissionDenyException(String message)
    {
        super(message);
    }
}
