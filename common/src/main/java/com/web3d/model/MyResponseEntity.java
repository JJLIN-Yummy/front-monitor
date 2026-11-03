package com.web3d.model;


import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class MyResponseEntity extends ResponseEntity {
    public MyResponseEntity() {
        super(HttpStatusCode.valueOf(200));
    }
}
