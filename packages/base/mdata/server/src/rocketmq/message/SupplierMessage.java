package com.guhecloud.cube.biz.mdata.server.rocketmq.message;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.RiskLevelEnum;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;

@Data
public class SupplierMessage extends BasicEntity<Long> {

    public final static String TOPIC = "SUPPLIER_SYNCHRONOUS";
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

    /**
     * 管理员用户名称
     */
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
     * 市场租户编号
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
