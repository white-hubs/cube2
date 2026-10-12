package com.guhecloud.cube.biz.mdata.server.grid.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 发起网格交接参数
 */
@Data
public class GridHandoverAddParam {

    /**
     * 网格区域id列表
     */
    @NotNull(message = "网格区域id列表不能为空")
    private List<Long> gridAreaIds;

    /**
     * 接班人
     */
    @NotNull(message = "接班人不能为空")
    private Long takeOverUser;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 交班人签名文件ID
     */
    private Long shiftSignFileId;

    /**
     * 扩展信息
     */
    @Valid
    private GridHandoverRecordExtendParam extend;
}
