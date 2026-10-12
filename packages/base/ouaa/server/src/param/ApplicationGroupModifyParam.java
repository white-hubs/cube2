package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class ApplicationGroupModifyParam {

    /**
     * 应用组名称
     */
    @NotEmpty(message = "应用组名称名称不能为空")
    @Size(max = 50, message = "应用组名称名称长度不能大于50")
    private String name;

    /**
     * 应用备注
     */
    private String remark;
}
