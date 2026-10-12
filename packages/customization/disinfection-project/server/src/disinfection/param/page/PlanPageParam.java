package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionPlanStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanPageParam extends Paging {
    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 状态
     */
    private DisinfectionPlanStatusEnum status;
    /**
     * 启用状态
     **/
    private DisinfectionEnableEnum enable;
    /**
     * 时间段 格式 2020-01-01 00:00:00,2020-01-01 23:59:59
     */
    private Period period;
}
