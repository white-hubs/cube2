package com.guhecloud.cube.biz.mdata.server.supplier.param;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.RiskLevelEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

/**
 * 批发商信息
 *
 * @author lzy
 * @date 2021/12/3
 */
@Data
public class SupplierModifyParam {

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
    private Long fileId;

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
    private String address;

    /**
     * 备注说明
     */
    @Size(max = 100, message = "备注说明不能大于100")
    private String remark;

    /**
     * 主营品种
     */
    //    @NotEmpty(message = "主营品种不能为空")
    private List<String> categoryIds;

    /**
     * 风险次数
     */
    private Integer riskAmount;

    /**
     * 风险级别
     */
    private RiskLevelEnum riskLevel;

    /**
     * 档位主键
     */
    private Set<Long> stallIds;

    /**
     * 批发商档主
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String shiftMaster;
    /**
     * 档主电话
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String shiftMasterPhone;
    /**
     * 档主身份证
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String shiftMasterCard;
    /**
     * 其他联系方人
     */
    @Size(max = 200, message = "详细地址不能大于200")
    private String otherContacts;

}
