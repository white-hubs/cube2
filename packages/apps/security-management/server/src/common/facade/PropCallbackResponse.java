package com.guhecloud.cube.srv.gen.securitymanagement.server.common.facade;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2023/11/3 9:50
 * @description
 */
@Data
public class PropCallbackResponse {

    private Long deviceId;

    List<Value> values;

    @Data
    public static class Value {
        private String key;
        private String value;
        private Long ts;
        private Object preValue;
    }

}
