package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

@Data
public class GetDeviceAttributeResponse {
    private String key;
    private String value;
    private Long ts;
    private Object preValue;
}
