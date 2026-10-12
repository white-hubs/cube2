package com.guhecloud.cube.srv.gen.supplier.server.supplier.param;

import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

@Data
public class GenSupplierModifyParam {
    /**
     * 商家名称
     */
    private String name;
    /**
     * 商家编码
     */
    private String code;
    /**
     * 管理员
     */
    private String adminPhone;

    /**
     * 企业LOGO文件主键
     */
    private Long fileId;

    /**
     * 联系人
     */
    private String liaison;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 所在地区集合
     */
    private List<String> areaIds;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 主营品种
     */
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
