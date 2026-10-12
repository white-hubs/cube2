package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-20 10:01
 */
@Data
public class KeyValueWithTs {

    private String key;

    private Object value;

    private Long ts;

}
