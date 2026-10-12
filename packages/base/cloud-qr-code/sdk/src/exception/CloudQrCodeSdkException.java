package com.guhecloud.cube.base.cloud.qr.code.sdk.exception;

/**
 * 云二维码SDK异常
 *
 * @author system
 */
public class CloudQrCodeSdkException extends RuntimeException {

    private String code;

    public CloudQrCodeSdkException(String message) {
        super(message);
    }

    public CloudQrCodeSdkException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CloudQrCodeSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudQrCodeSdkException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
