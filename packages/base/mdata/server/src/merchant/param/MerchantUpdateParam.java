package com.guhecloud.cube.biz.mdata.server.merchant.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 商品更新信息
 */
@Data
public class MerchantUpdateParam {

    /**
     * 主键ID
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 商户编号
     */
    @NotEmpty(message = "编号不能为空")
    @Size(max = 20, message = "编号不能大于20")
    private String code;

    /**
     * 商户名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 20, message = "名称不能大于20")
    private String name;

    /**
     * 联系人姓名
     */
    @NotEmpty(message = "联系人不能为空")
    @Size(max = 20, message = "联系人不能大于20")
    private String liaison;

    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    @Size(max = 20, message = "手机号不能大于20")
    private String phone;

    /**
     * 区域主键集合
     */
    @NotEmpty(message = "区域不能为空")
    @Size(max = 100)
    private List<String> areaIds;

    /**
     * 详细地址
     */
    @NotEmpty(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址不能大于200")
    private String address;

    /**
     * 文件主键
     */
    private Long fileId;

}
