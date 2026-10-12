package com.guhecloud.cube.srv.gen.maintain.server.common.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author wql
 * @describe 统计查询标准参数
 * @date 2024/1/24 18:53
 */
@Data
public class StatisticsQueryParam {
    /**
     * 统计时间范围
     */
    private Period period;
    /**
     * 时间类型
     */
    private String type;

    private String dateExp;

    private String dateString;

    private String viewDateString;
}
