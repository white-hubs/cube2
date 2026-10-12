package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import lombok.Data;

/**
 * 商户信息
 *
 * @author lanchun
 * @date 2023/8/3 14:04
 */
@Data
public class SupplierInfoVO {

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
     * 头像信息
     */
    private FileVO fileVO;
}
