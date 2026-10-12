package com.guhecloud.cube.biz.mdata.client.dto;

import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家编号
     */
    private String code;

    /**
     * 管理员用户主键
     */
    private Long adminUserId;
    private String adminUserName;

    /**
     * 管理员
     */
    private String adminPhone;

    /**
     * 联系人
     */
    private String liaison;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所属地区
     */
    private String area;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 主营品种
     */
    private String category;

    /**
     * 批发商风险级别状态
     */
    private RiskLevelEnum riskLevel;

    /**
     * 风险次数
     */
    private Integer riskAmount;

    /**
     * 批发商租户编号
     */
    private Integer supplierTenant;

    /**
     * 市场方租户编号
     */
    private Integer tenant;

    /**
     * 批发商档主
     */
    private String shiftMaster;
    /**
     * 档主电话
     */
    private String shiftMasterPhone;
    /**
     * 档主身份证
     */
    private String shiftMasterCard;
    /**
     * 其他联系方人
     */
    private String otherContacts;

}
