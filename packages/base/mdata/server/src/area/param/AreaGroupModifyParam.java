package com.guhecloud.cube.biz.mdata.server.area.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 区域组保存参数
 *
 * @author lanchun
 * @date 2022/4/15 14:33
 */
@Data
public class AreaGroupModifyParam {

    /**
     * 区域组名称
     */
    @NotNull(message = "区域组名称")
    @Size(max = 20, message = "区域组名称不能大于20")
    private String name;

    /**
     * 父类主键
     */
    private Long parentId;

    /**
     * 是否启用
     */
    private Boolean enabled = true;
}
