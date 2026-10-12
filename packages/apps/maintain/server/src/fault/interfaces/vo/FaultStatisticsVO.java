package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe app故障报修看板统计
 * @date 2023/11/6 14:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FaultStatisticsVO {
    /**
     * 故障报修总数
     */
    private Integer total;
    /**
     * 处理成功数
     */
    private Integer success;
}
