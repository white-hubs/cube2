package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantUserEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 商户用户查询信息
 *
 * @author lzy
 * @date 2021/10/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantUserQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 商户主键不能为空
     */
    @NotNull(message = "商户主键不能为空")
    private Long merchantId;

    /**
     * 商户用户角色类型
     */
    @NotNull(message = "商户用户角色类型")
    private MerchantUserEnum merchantCategory;

}
