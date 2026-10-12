package com.guhecloud.cube.biz.mdata.server.purchaser.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2021/12/31
 */
@Data
public class PurchaserModifyParam {

    @NotNull(message = "市场租户编号不能为空")
    private Integer tenant;

}
