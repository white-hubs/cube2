package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author wql
 * @describe 隐患统计参数
 * @date 2023/11/12 13:39
 */
@Data
public class HiddenTaskStatisticsParam {
    /**
     * 时间参数
     */
    private Period period;
}
