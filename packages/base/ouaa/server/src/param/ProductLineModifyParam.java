package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 产品线保存参数
 *
 * @author lanchun
 * @date 2021/10/13 11:59
 */
@Data
public class ProductLineModifyParam {

    /**
     * 产品线名称
     */
    @NotEmpty(message = "产品线名称不能为空")
    @Size(max = 100, message = "产品线名称不能大于100")
    private String name;

    /**
     * 产品线备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = true;
}
