package com.guhecloud.cube.srv.gen.maintain.dto;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 巡更点查询参数
 *
 * @author lanchun
 * @date 2025/6/19 16:39
 */
@Data
public class LocationQueryParam {

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 巡更结果
     */
    private String result;
}
