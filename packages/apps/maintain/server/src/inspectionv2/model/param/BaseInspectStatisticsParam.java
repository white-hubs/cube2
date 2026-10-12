package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 19:31
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseInspectStatisticsParam extends Paging {
    /**
     * 时间范围
     */
    private Period period;

    /**
     * 日期类型(时间类型 TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年')
     */
    private String type;
    /**
     * 是否为根据权限查询
     */
    private Boolean admin = true;

    private String businessType;

    private String result;
}
