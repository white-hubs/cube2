package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LineLossAlarmRuleBasicVO {
    private Long id;
    /**
     * 触发的规则id
     */
    private Long triggeredRuleId;
    private String name;
    /**
     * 报警级别
     */
    private Long alarmLevelId;
    private String alarmLevelName;
    /**
     * 处理时限值
     */
    private Integer estimatedHandlerTime;
    /**
     * 处理时限名称
     */
    private String estimatedHandlerTimeName;
    /**
     * 处理时限单位
     */
    private String estimatedHandlerUnit;
    /**
     * 事件类型id
     */
    private Long emergencyEventTypeId;
    /**
     * 事件类型名称
     */
    private String emergencyEventTypeName;
    /**
     * 触发条件,符合和值用逗号隔开，多组条件用分号隔开即：符合,值;符合,值(例如：EQ,10;GE,12)
     */
    private String condition;
    private String description;
    /**
     * 线路id集合
     */
    private List<Long> lineIdList;
    /**
     * 线路集合
     */
    private List<LineLossConnection> lineList;
    private String createBy;
    private LocalDateTime createTime;
}
