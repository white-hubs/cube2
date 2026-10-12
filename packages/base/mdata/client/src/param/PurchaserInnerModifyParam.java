package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2022/2/18
 */
@Data
@Builder
public class PurchaserInnerModifyParam {

    /**
     * 用户名称
     */
    @NotEmpty(message = "用户名称不能为空")
    private String name;

    /**
     * 用户电话
     */
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    /**
     * 市场租户编号
     */
    @NotNull(message = "市场租户编号不能为空")
    private Integer tenant;
}
