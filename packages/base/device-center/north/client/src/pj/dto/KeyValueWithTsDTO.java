package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

/**
 * 承载某刻的属性值
 */
@Data
public class KeyValueWithTsDTO {

    private String key;

    private Object value;

    private Long ts;
}
