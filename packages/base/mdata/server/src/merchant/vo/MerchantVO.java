package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.RisklevelEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品基本信息
 */
@Data
public class MerchantVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称（商户管理员名称）
     */
    private String adminName;
    /**
     * 商户编号
     */
    private String code;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 是否为买家
     */
    private Boolean buyer;
    /**
     * 是否为卖家
     */
    private Boolean seller;
    /**
     * 联系人姓名
     */
    private String liaison;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 区域集合
     */
    private List<String> areaIds;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 审核状态
     */
    private MerchantEnum status;
    /**
     * 商户认证信息
     */
    private Long authenticationId;
    /**
     * 经营性质
     */
    private MerchantEnum type;
    /**
     * 经营者名称
     */
    private String authenticationName;
    /**
     * 身份证号
     */
    private String identityCard;
    /**
     * 身份证图片
     */
    private List<FileVO> fileVOS;
    /**
     * 业务角色集合
     */
    private List<MerchantEnum> bizTypes;

    /**
     * 商品品类主键集合
     */
    private List<String> goodsCategoryIds;

    /**
     * 主营品种集合
     */
    private List<GoodsCategory> goodsCategories;

    /**
     * 买家认证状态
     */
    private MerchantEnum buyerStatus;

    /**
     * 卖家认证状态
     */
    private MerchantEnum sellerStatus;

    /**
     * 风险级别
     */
    private RisklevelEnum riskLevel;

    /**
     * 备注
     */
    private String remark;

    /**
     * logo
     */
    private FileVO logo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
