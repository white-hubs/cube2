package com.guhecloud.cube.base.alarm.client.param;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 报警统计查询参数
 *
 * @author lanchun
 * @date 2022/6/8 13:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmStatisticsQueryParam {

    private String type;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 报警对象编号
     */
    private String alarmTargetCode;

    /**
     * 报警对象类型主键
     */
    private String alarmTargetTypeId;

    private String area;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 是否模糊匹配区域，默认为 true
     */
    private Boolean areaLike;

    /**
     * 报警级别
     */
    private String alarmLevelId;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;

    /**
     * 区域序列号集合
     */
    private List<String> areaSequences;

    /**
     * 用户主键集合
     */
    private List<Long> userIds;

    /**
     * 报警类型主键集合
     */
    private List<Long> alarmTypeIds;

    /**
     * 事件主键集合
     */
    private List<String> eventIds;

    private List<String> deviceTypes;

    private List<String> departmentIds;

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
     * 报警处理结果
     */
    private String alarmHandlerResult;

    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 应用集合
     */
    private List<String> applicationCodeList;

    /**
     * 报警事件分类id
     */
    private String alarmEventTypeId;

    /**
     * 报警事件分类id集合
     */
    private Set<String> alarmEventTypeIds;

    public String getArea() {
        if (StringUtils.isNotEmpty(this.area)) {
            return this.area.substring(0, this.area.lastIndexOf("-"));
        }
        return null;
    }
}
