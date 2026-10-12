package com.guhecloud.cube.biz.adapter.broadcast.server.exception;

import lombok.Getter;

@Getter
public class BroadcastException extends RuntimeException {

    private final String code;

    public BroadcastException(String code, String message) {
        super(message);
        this.code = code;
    }
}
