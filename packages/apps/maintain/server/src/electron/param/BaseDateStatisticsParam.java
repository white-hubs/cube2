package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class BaseDateStatisticsParam {

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

    /**
     * 区域id列表
     */
    private List<Long> areaIds;
}
