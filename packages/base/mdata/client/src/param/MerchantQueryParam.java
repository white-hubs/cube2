package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.cube.biz.mdata.client.enumeration.MerchantEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 商品查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantQueryParam extends Paging {
    /**
     * 商户名称（pc端查询）
     */
    @Size(max = 20, message = "商户名称不能大于20")
    private String keyword;
    /**
     * 经营类型
     */
    private MerchantEnum type;
    /**
     * 业务类型
     */
    private MerchantEnum bizType;
    /**
     * 审核状态
     */
    private MerchantEnum auditStatus;
    /**
     * 商户主键集合
     */
    private List<Long> merchantIds;
    /**
     * 平台
     */
    private String platform;

}
