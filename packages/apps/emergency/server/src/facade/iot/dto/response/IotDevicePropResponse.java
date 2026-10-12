package com.guhecloud.cube.biz.emergency.server.facade.iot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2024/1/2 9:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IotDevicePropResponse {

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性值
     */
    private String propValue;
}
