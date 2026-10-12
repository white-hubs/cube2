package com.guhecloud.cube.srv.gen.maintain.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 16:44
 * @Description:
 */
@Data
public class HiddenWorkOrderStatisticsDTO extends Paging {
    /**
     * 部门id
     */
    private Long hiddenDangerTypeId;
    /**
     * 开始时间-结束时间
     */
    private Period period;
    private String applicationCode;
}
