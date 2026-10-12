package com.guhecloud.cube.biz.mdata.server.area.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 区域组下关联区域参数
 *
 * @author lanchun
 * @date 2022/4/18 14:23
 */
@Data
public class AreaGroupConfigModifyParam {

    /**
     * 区域组主键
     */
    @NotNull(message = "区域组主键不能为空")
    @Min(value = 1, message = "区域组主键不能小于1")
    private Long areaGroupId;

    /**
     * 区域组序列号
     */
    private String areaGroup;

    /**
     * 区域主键
     */
    @NotNull(message = "区域主键不能为空")
    @Min(value = 1, message = "区域主键不能小于1")
    private Long areaId;

    /**
     * 区域序列号
     */
    private String area;
}
