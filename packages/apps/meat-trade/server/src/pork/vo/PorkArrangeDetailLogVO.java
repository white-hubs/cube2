package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PorkArrangeDetailLogVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 报备号
     */
    private String porkReportCode;

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
    private BigDecimal standardWeight;

    /**
     * 重量是否达标
     */
    private Boolean weightCompliant;

    /**
     * 销售价格
     */
    private String price;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货时间
     */
    private LocalDateTime porkArrangeTime;

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
     * 是否合格
     */
    private Boolean passed;

    /**
     * 是否作弊
     */
    private Boolean cheat;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 理货图片路径
     */
    private String picPaths;

    /**
     * 转存的附件信息
     */
    private String transferFiles;

    /**
     * 图片文件地址
     */
    private List<String> picFilePaths;
}
