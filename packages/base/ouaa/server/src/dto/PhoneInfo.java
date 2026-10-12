package com.guhecloud.cube.biz.ouaa.server.dto;

import lombok.Data;

@Data
public class PhoneInfo {
    /**
     * {
     * "phoneNumber" : "13683421469",
     * "purePhoneNumber" : "13683421469",
     * "countryCode" : "86",
     * "watermark" : {
     * "timestamp" : 1760498246,
     * "appid" : "wxd243a4ca10bf9d8d"
     * }
     * }
     */
    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
    private Watermark watermark;

    @Data
    public static class Watermark {
        private long timestamp;
        private String appid;
    }
}