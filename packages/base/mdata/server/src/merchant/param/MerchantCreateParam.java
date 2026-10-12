package com.guhecloud.cube.biz.mdata.server.merchant.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 商品新增信息
 */
@Data
public class MerchantCreateParam {

    /**
     * 主键
     */
    private Long id;
    /**
     * 商户名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "商户名称不能大于100")
    private String name;
    /**
     * 联系人姓名
     */
    @NotEmpty(message = "联系人不能为空")
    @Size(max = 10, message = "联系人姓名不能大于10")
    private String liaison;
    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    @Size(max = 20, message = "手机号码不能大于20")
    private String phone;

    /**
     * 区域主键集合
     */
    @NotEmpty(message = "区域不能为空")
    private List<String> areaIds;
    /**
     * 详细地址
     */
    @NotEmpty(message = "详细地址不能为空")
    @Size(max = 100, message = "详细地址不能大于100")
    private String address;
}
