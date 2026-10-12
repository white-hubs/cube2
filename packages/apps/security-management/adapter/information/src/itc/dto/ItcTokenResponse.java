package com.guhecloud.cube.srv.gen.adapter.information.server.itc.dto;

import lombok.Data;

@Data
public class ItcTokenResponse {
    private Ocs ocs;

    @Data
    public static class Meta {
        private String status;
        private Integer statuscode;
        private String message;
    }

    @Data
    public static class Ocs {
        private Meta meta;
        private Object data;
    }
}
