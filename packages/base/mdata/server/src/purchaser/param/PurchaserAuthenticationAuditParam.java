package com.guhecloud.cube.biz.mdata.server.purchaser.param;

import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2021/12/29
 */
@Data
public class PurchaserAuthenticationAuditParam {

    /**
     * 认证状态
     */
    @NotNull(message = "认证状态不能为空")
    private PurchaserAuthenticationEnum status;

    @Size(max = 200, message = "备注不能大于200")
    private String remark;

}
