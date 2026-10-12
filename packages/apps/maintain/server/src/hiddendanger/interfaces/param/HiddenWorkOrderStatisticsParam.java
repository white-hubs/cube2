package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  15:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class HiddenWorkOrderStatisticsParam extends Paging {
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
