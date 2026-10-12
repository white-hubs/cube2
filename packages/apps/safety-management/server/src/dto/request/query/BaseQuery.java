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
 * @author wql
 * @describe 安全管理卡片
 * @date 2023/9/14 11:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseQuery extends Paging {
    /**
     * 查询时间范围
     */
    private Period period;

    /**
     * 查询任务创建时间范围
     */
    private Period taskCreatePeriod;

    /**
     * 时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
     * THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * 完成时限
     */
    private Period completionPeriod;

    /**
     * 市场租户
     */
    private Integer marketTenant;
    private List<Integer> marketTenants;
    /**
     * 风险源类型
     */
    private String typeCode;
    /**
     * 风险源子类型
     */
    private String subTypeCode;

    private String keyword;
    private Boolean expired = false;

    /**
     * 巡检业务类型
     */

    private String businessType;
    /**
     * 异常项集合
     */
    private List<String> errorTagList;
    /**
     * 逗号分割的异常项
     */
    private String errorTags;
}
