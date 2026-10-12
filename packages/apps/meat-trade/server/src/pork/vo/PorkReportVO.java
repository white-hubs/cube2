package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Data
public class PorkReportVO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 报备号
     */
    private String code;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;

    /**
     * 批发商编号
     */
    private String supplierCode;

    /**
     * 档位主键
     */
    private Long stallId;

    /**
     * 档位名称
     */
    private String stallName;

    /**
     * 屠宰场主键
     */
    private Long abattoirId;

    /**
     * 屠宰场名称
     */
    private String abattoirName;

    /**
     * 屠宰场编号
     */
    private String abattoirCode;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 报备片数
     */
    private Integer reportSlices;

    /**
     * 报备重量
     */
    @Decimal
    private BigDecimal reportWeight;

    /**
     * 报备时间
     */
    private LocalDateTime reportTime;

    /**
     * 报备商品主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 到达时间
     */
    private LocalDateTime arrivedTime;

    /**
     * 车辆号码
     */
    private String vehiclePlate;

    /**
     * 检疫证号
     */
    private String pratiqueCode;

    /**
     * 检疫证号
     */
    private List<String> pratiqueCodes;

    /**
     * 检疫证号图片集合
     */
    private List<FileVO> files;

    /**
     * 产地名称
     */
    private String originPlace;

    /**
     * 报备状态
     */
    private PorkReportEnum status;

    /**
     * 审核人主键
     */
    private Long auditorId;

    /**
     * 审核人名称
     */
    private String auditorName;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 批发商风险级别
     */
    private RiskLevelEnum riskLevel;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 理货码
     */
    private String barCode;

    /**
     * 租户编号
     */
    private Integer tenant;

}
