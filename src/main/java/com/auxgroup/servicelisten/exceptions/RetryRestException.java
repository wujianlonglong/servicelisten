package com.auxgroup.servicelisten.exceptions;


import org.springframework.web.client.RestClientException;

public class RetryRestException extends RestClientException {
    public RetryRestException(String msg) {
        super(msg);
    }
}
