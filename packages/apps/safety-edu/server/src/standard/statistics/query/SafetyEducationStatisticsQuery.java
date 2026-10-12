package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 安全教育统计查询参数请求
 *
 * @author lanchun
 * @date 2024/1/22 17:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SafetyEducationStatisticsQuery extends Paging {

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
     * THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
     */
    private String type;
}
