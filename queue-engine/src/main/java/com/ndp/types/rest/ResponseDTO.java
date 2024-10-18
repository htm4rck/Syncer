package com.ndp.types.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO<T> {
    private boolean successful;
    private Data<T> data;
    public ResponseDTO(boolean successful, Data<T> data) {
        this.successful = successful;
        this.data = data;
    }
}

