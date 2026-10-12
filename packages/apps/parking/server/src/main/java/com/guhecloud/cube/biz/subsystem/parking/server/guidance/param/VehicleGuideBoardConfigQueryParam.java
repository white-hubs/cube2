package com.guhecloud.cube.biz.subsystem.parking.server.guidance.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆引导牌配置查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VehicleGuideBoardConfigQueryParam extends Paging {

    /**
     * 名称（模糊搜索）
     */
    private String name;
}

