package com.guhecloud.cube.srv.gen.safety.management.report.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class AlarmRecordResponseVO {
    /**
     * 排名
     */
    private Integer index;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 一级报警数
     */
    private Long levelAlarmNum;
    /**
     * 部门下一级报警数
     */
    private Long DepAlarmNum;
    private Long departmentNum;

    /**
     * 人员负责报警数
     */
    private Long userAlarmNum;
    /**
     * 已响应报警数
     */
    private Long respondedNum;
    /**
     * 已处理报警数
     */
    private Long alarmHandlerNum;
    /**
     * 超时数据
     */
    private Long timeoutNum;

    /**
     * 跨天处理数据
     */
    private Long outDayHandlerNum;

    /**
     * 响应率
     */
    private String respondRatio;
    private BigDecimal respondRatioNum;

    /**
     * 处理率
     */
    private String alarmHandlerRatio;
    private BigDecimal alarmHandlerRatioNum;
    /**
     * 超时率
     */
    private String timeoutRatio;
    private BigDecimal timeoutRatioNum;
    /**
     * 跨天处理率
     */
    private String outDayHandlerRatio;
    private BigDecimal outDayHandlerRatioNum;
}
