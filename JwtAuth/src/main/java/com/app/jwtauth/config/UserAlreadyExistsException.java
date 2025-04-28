package com.app.jwtauth.config;

import org.hibernate.service.spi.ServiceException;

public class UserAlreadyExistsException extends ServiceException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}


