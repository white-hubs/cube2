package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/30 15:58
 * @Description:
 */

@Data
public class BaseDateStatisticsParam {

    /**
     * 时间范围
     */
    private Period period;
}
