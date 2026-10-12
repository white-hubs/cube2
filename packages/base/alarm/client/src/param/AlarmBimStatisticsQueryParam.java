package com.guhecloud.cube.base.alarm.client.param;

import lombok.Data;

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
     * 时间类型 TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
     */
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
     * 时间范围
     */
    private String period;

    /**
     * 报警对象编号
     */
    private String alarmTargetCode;

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
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用集合
     */
    private List<String> applicationCodeList;

    /**
     * 报警事件分类id集合
     */
    private Set<String> alarmEventTypeIds;
    //事件名称
    private String eventName;

    private String timeFormat;
}
