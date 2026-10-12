package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

/**
 * 三维统计查询参数
 *
 * @author lanchun
 * @date 2022/8/9 17:38
 */
@Data
public class AlarmBimStatisticsQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    private String type;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 区域级别
     */
    private Integer areaLevel;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 报警对象编号
     */
    private String alarmTargetCode;
    /**
     * 时件名称
     */
    private String eventName;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 报警状态
     */
    private String alarmStatus;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用集合
     */
    private List<String> applicationCodeList;

    private String timeFormat;

    /**
     * 设备标签集合
     */
    private Set<String> deviceTags;

    /**
     * 报警事件分类id
     */
    private String alarmEventTypeId;

    /**
     * 报警时间分类id集合
     */
    private Set<String> alarmEventTypeIds;
}
