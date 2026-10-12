package com.guhecloud.cube.biz.mdata.server.grid.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 批量设置网格员参数
 */
@Data
public class GridAreaMemberBatchSetParam {

    /**
     * 网格区域id
     */
    @NotNull(message = "网格区域id不能为空")
    private Long gridAreaId;

    /**
     * 网格员id列表
     */
    @NotNull(message = "网格员id列表不能为空")
    private List<Long> memberIds;
}
