package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.database.entity.Creatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理货数据详情记录信息
 */
@Data
@TableName(value = "meattrade_pork_arrange_detail_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PorkArrangeDetailLog implements Creatable<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 理货编号
     */
    private String code;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备号
     */
    private String porkReportCode;

    /**
     * 商品品类
     */
    private String goodsCategoryId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 商品主键
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 电子标签
     */
    private String label;

    /**
     * 脚标编码
     */
    private String footMarkCode;

    /**
     * 净重
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 标准重量
     */
    @Decimal
    private BigDecimal standardWeight;

    /**
     * 重量是否达标
     */
    private Boolean weightCompliant;

    /**
     * 销售价格
     */
    @Decimal
    private BigDecimal price;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 是否作弊
     */
    private Boolean cheat;

    /**
     * 理货图片路径
     */
    private String picPaths;

    private String transferFiles;

    /**
     * 转存次数
     */
    private Integer transferNumber;

    /**
     * 转存状态
     */
    private Boolean transferStatus;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
