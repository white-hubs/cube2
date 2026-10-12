package com.guhecloud.cube.iot.mgt.comm.excel;

public class ExcelException extends RuntimeException{
    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
