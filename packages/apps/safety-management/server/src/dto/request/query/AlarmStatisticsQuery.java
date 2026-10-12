package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

/**
 * 报警统计查询参数请求
 *
 * @author lanchun
 * @date 2023/9/8 10:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AlarmStatisticsQuery extends Paging {

    /**
     * 时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
     * THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * mysql日期格式
     */
    private String dateExp;

    /**
     * 格式化格式
     */
    private String dateString;

    /**
     * 展示的日期格式
     */
    private String viewDateString;

    /**
     * 报警级别
     */
    private String alarmLevenCode;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 报警类型编号
     */
    private String alarmTypeCode;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 报警状态
     */
    private String status;

    /**
     * 过滤的状态
     */
    private String filterStatus;

    /**
     * 报警超时状态
     */
    private String alarmHandlerTimeoutStatus;
    /**
     * 报警事件分类编号
     */
    private String alarmEventTypeCode;

    /**
     * 报警事件分类编号集合
     */
    private List<String> alarmEventTypeCodes;

    /**
     * 市场租户编号集合
     */
    private Set<Integer> marketTenants;
}
