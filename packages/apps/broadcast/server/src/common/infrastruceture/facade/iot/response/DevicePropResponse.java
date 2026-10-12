package com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 设备属性返回参数
 *
 * @author lanchun
 * @date 2023/11/7 15:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicePropResponse {

    /**
     * 设备主键
     */
    private Long id;

    /**
     * 设备属性
     */
    private List<PropKeyValue> propKeyValues;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropKeyValue {

        /**
         * 属性编号
         */
        private String propCode;

        /**
         * 数值值
         */
        private Object propValue;
    }
}
