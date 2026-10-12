package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 消防设备统计
 * @date 2023/9/8 13:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FireFightingDeviceStatisticsVO {
    /**
     * 消防设备总数
     */
    private Integer totalNum;
    /**
     * 消防设备状态
     */
    private List<FireFightingDeviceState> deviceStateList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FireFightingDeviceState {
        /**
         * 状态
         */
        private String state;
        /**
         * 状态名称
         */
        private String stateName;
        /**
         * 状态数量
         */
        private Long stateNum;
        /**
         * 占比
         */
        private Double ratio;
    }
}
