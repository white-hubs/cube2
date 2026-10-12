package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品基本信息
 */
@Data
public class AuthenticationVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商户主键
     */
    private Long merchantId;
    /**
     * 经营性质
     */
    private MerchantEnum type;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String identityCard;
    /**
     * 经营主体类型
     */
    private MerchantEnum subjectType;
    /**
     * 主体名称
     */
    private String subjectName;
    /**
     * 经营证照号
     */
    private String licenseNo;
    /**
     * 审核状态
     */
    private MerchantEnum status;
    /**
     * 审核不通过原因
     */
    private String reason;
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 联系人
     */
    private String liaison;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 文件信息
     */
    private List<FileVO> fileVOS;
    /**
     * 商品品类集合
     */
    private List<GoodsCategory> goodsCategories;
    /**
     * 卖家认证提交认证
     */
    private LocalDateTime sellerSubmitTime;
    /**
     * 卖家认证状态
     */
    private MerchantEnum sellerStatus;
    /**
     * 卖家认证不通过原因
     */
    private String sellerReason;
}
