package com.guhecloud.cube.biz.mdata.server.supplier.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Data
public class SupplierVO {

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
     * 管理员
     */
    private String admin;

    /**
     * 管理员用户主键
     */
    private Long adminUserId;

    /**
     * 管理员名称
     */
    private String adminName;

    /**
     * 管理员手机号
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
    private List<String> areaIds;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 认证状态
     */
    private SupplierAuthenticatedEnum status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 主营品种主键
     */
    private List<String> categoryIds;

    /**
     * 主营品种名称
     */
    private List<String> categoryName;

    /**
     * 主营品种
     */
    private List<GoodsCategory> categories;

    /**
     * logo主键
     */
    private Long logoId;

    /**
     * file主键
     */
    private Long fileId;

    /**
     * logo图片
     */
    private FileVO logo;

    /**
     * 商户租户编号
     */
    private Integer supplierTenant;

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketTenantName;

    /**
     * 商户角色
     */
    private SupplierUserEnum supplierCategory;

    /**
     * 授权报备
     */
    private Boolean authPurchaseReport;

    /**
     * 授权余额支付
     */
    private Boolean authWalletPay;

    /**
     * 冗余字段：转用于前端组件选中使用
     */
    private String useForSelect;

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
