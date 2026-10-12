package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

@Data
public class KeyValueWithTsVo {
    private String key;

    private String keyName;

    private Object value;

    private Long ts;
}
