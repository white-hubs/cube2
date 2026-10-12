package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 产品保存参数
 *
 * @author lanchun
 * @date 2021/10/14 14:14
 */
@Data
public class ProductModifyParam {

    /**
     * 产品编号
     */
    @NotEmpty(message = "产品编号不能为空")
    @Size(max = 100, message = "产品编号不能大于100")
    private String code;

    /**
     * 产品线主键
     */
    @NotNull(message = "产品线主键不能为空")
    @Min(value = 1, message = "产品线主键不能小于1")
    private Long productLineId;

    /**
     * 产品名称
     */
    @NotEmpty(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称不能大于100")
    private String name;

    /**
     * 产品域名
     */
    @Size(max = 100, message = "产品域名不能大于100")
    private String domain;

    /**
     * 产品备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

}
