package com.guhecloud.cube.biz.mdata.server.grid.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 网格接班参数
 */
@Data
public class GridHandoverTakeOverParam {
    /**
     * 交接记录ID
     */
    @NotNull(message = "交接记录ID不能为空")
    private Long id;

    /**
     * 接班人签名文件ID
     */
    private Long takeSignFileId;
}
