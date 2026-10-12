package com.guhecloud.cube.biz.mdata.server.supplier.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Data
public class SupplierModifyMpParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商家名称
     */
    @NotEmpty(message = "商家名称不能为空")
    @Size(max = 100, message = "商家名称不能大于100")
    private String name;
    /**
     * 管理员
     */
    @NotEmpty(message = "管理员手机号不能为空")
    @Size(max = 20, message = "管理员手机号不能大于20")
    private String adminPhone;

    /**
     * 企业LOGO文件主键
     */
    private Long logoId;

    /**
     * 联系人
     */
    @NotEmpty(message = "联系人不能为空")
    @Size(max = 100, message = "联系人不能大于100")
    private String liaison;

    /**
     * 联系人手机号
     */
    @NotEmpty(message = "联系人手机号不能为空")
    @Size(max = 100, message = "联系人手机号不能大于100")
    private String phone;

    /**
     * 所在地区集合
     */
    @NotEmpty(message = "所在地区不能为空")
    private List<String> areaIds;

    /**
     * 详细地址
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String address = "";

    /**
     * 备注说明
     */
    @Size(max = 100, message = "备注说明不能大于100")
    private String remark;

    /**
     * 主营品种
     */
    private List<String> categoryIds;

    /**
     * 市场方租户编号
     */
    private Integer marketTenant;

    /**
     * 批发商租户编号
     */
    private Integer supplierTenant;

    /**
     * 档位主键
     */
    private Set<Long> stallIds;

}
