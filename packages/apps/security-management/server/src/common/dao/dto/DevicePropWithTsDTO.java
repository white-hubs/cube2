package com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2023/10/31 16:05
 * @description
 */
@Data
public class DevicePropWithTsDTO {
    private String key;
    private Object value;
    private String ts;
    private String unitName;
    private String translatedValue;
}
