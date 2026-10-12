package com.guhecloud.cube.biz.mdata.server.purchaser.param;

import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2021/12/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaserAuthenticationQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 认证状态
     */
    private PurchaserAuthenticationEnum status;

    /**
     * 认证类型
     */
    private PurchaserTypeEnum type;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
