package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import java.util.List;

/**
 * 小程序商户信息
 *
 * @author lzy
 * 2021/9/6
 */
@Data
public class MerchantAppVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户主键
     */
    private Long userId;
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
     * 经营主体类型
     */
    private String subjectType;
    /**
     * 主体名称
     */
    private String subjectName;
    /**
     * 经营证照号
     */
    private String licenseNo;
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
     * 备注
     */
    private String remark;
}
