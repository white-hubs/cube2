package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

@Data
public class QueryDeviceByAreaAndTagParam {

    /**
     * 标签编号集合
     */
    @NotNull(message = "标签编号")
    @Size(min = 0, message = "标签编号不能为空")
    private List<String> tagCodes;

    /**
     * 区域id集合
     */
    @NotNull(message = "区域层级不能为空")
    private Set<Long> areaIds;
}
