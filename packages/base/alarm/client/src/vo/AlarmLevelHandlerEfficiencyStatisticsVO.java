package com.guhecloud.cube.base.alarm.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警级别处理效率返回参数
 *
 * @author lanchun
 * @date 2022/11/10 15:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelHandlerEfficiencyStatisticsVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 报警编号
     */
    private String code;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 级别序号
     */
    private Integer sort;

    /**
     * 已处理报警数量
     */
    private Long handlerNum;

    /**
     * 总时长
     */
    private Long durationTotal;

    /**
     * 平均处理时长
     */
    private Float avgHandlerDuration;

    /**
     * 处理效率最高用户名
     */
    private String highEfficiencyUserName;

    /**
     * 处理效率最高用户平均处理时长
     */
    private Long highEfficiencyUserAvgDuration;

    /**
     * 处理效率最低用户名
     */
    private String lowEfficiencyUserName;

    /**
     * 处理效率最低用户平均处理时长
     */
    private Long lowEfficiencyUserAvgDuration;
}
