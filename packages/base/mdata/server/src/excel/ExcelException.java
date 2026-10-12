package com.guhecloud.cube.biz.mdata.server.excel;

public class ExcelException extends RuntimeException {
    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
