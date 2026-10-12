package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import java.util.List;

/**
 * 商品基本信息
 */
@Data
public class MerchantPageVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 用户主键
     */
    private Long owner;
    /**
     * 业务类型
     */
    private List<MerchantEnum> bizTypes;
    /**
     * 联系人姓名
     */
    private String liaison;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 审核状态
     */
    private MerchantEnum status;
    /**
     * 经营类型
     */
    private MerchantEnum type;
    /**
     * 地址
     */
    private String address;
    /**
     * 钱包主键
     */
    private Long walletId;

    /**
     * logo图片
     */
    private FileVO logo;

    /**
     * 管理员名称
     */
    private String adminName;

    /**
     * 商品分类集合
     */
    private List<GoodsCategory> goodsCategories;
}
