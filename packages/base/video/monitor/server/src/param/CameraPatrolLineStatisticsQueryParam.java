package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 巡逻异常情况统计参数
 *
 * @author lanchun
 * @date 2022/11/9 14:15
 */
@Data
public class CameraPatrolLineStatisticsQueryParam {

    //时间类型 TODAY:'今日',THIS_WEEK:'本周',THIS_MONTH:'本月',NEAR_THREE_MONTH:'近3月',NEAR_HALF_YEAR:'近半年',THIS_YEAR:'本年'
    private String type;

    /**
     * 时间范围
     */
    //    @NotNull(message = "时间范围不能为空")
    private Period period;
}
