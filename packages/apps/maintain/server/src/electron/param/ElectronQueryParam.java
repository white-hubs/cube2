package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 19:16
 * @Description:
 */
@Data
public class ElectronQueryParam {

    /**
     * 时间范围
     */
    private Period period;
    /**
     * 巡更点名称
     */
    private String pointName;

    /**
     * 区域名称
     */
    private String areaName;
}
