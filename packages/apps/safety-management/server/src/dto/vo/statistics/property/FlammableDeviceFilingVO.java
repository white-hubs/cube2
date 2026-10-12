package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 易燃设备建档统计
 * @date 2023/9/13 18:47
 */
@Data
public class FlammableDeviceFilingVO {
    /**
     * 设备总数
     */
    private Integer totalNum;
    /**
     * 设备状态占比
     */
    private List<FlammableDeviceStatusVO> statusVOList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FlammableDeviceStatusVO {
        /**
         * 设备状态
         */
        private String deviceState;
        /**
         * 数量
         */
        private Integer stateNum;
        /**
         * 状态占比
         */
        private Double ratio;
    }
}
