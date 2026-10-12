package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 故障报告概览
 * @date 2023/11/9 11:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FaultReportOverviewVO {
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 故障单名称
     */
    private String name;
    /**
     * 故障设备对象名称
     */
    private String objName;
    /**
     * 故障来源
     */
    private String resource;
    /**
     * 提交人
     */
    private String submitter;
    /**
     * 状态
     */
    private String faultStatus;
    /**
     * 处理结果
     */
    private String faultResult;
}
