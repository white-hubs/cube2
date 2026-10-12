package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

/**
 * 键值对
 *
 * @author fl
 * @date 2022/11/15
 */
@Data
public class Entry {
    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private String value;
}
