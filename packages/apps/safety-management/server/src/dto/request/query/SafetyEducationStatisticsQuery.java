package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 安全教育统计查询参数请求
 *
 * @author lanchun
 * @date 2023/9/8 10:12
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
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
     * THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 考试类型
     */
    private String examType;

    /**
     * 培训类型
     */
    private String cultivateType;

    /**
     * 培训形式(线上、线下)
     */
    private String cultivateMode;

    /**
     * 统计的市场范围
     */
    private List<String> marketTenantIDs;

    /**
     * 关键字
     */
    private String keyword;
}
