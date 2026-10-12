package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 巡逻路线统计参数
 *
 * @author lanchun
 * @date 2022/11/9 13:44
 */
@Data
public class CameraPatrolLineStatisticsPageQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 时间范围
     */
    private Period period;

}
