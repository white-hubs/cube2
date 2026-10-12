package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/6
 */
@Data
public class SpotCheckVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备编号
     */
    private String porkReportCode;

    /**
     * 报备片数
     */
    private Integer porkReportSlices;

    /**
     * 报备商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 档位名称
     */
    private String stallName;

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
     * 风险级别
     */
    private RiskLevelEnum riskLevel;

    /**
     * 抽检员主键
     */
    private Long inspectorId;

    /**
     * 抽检员名称
     */
    private String inspectorName;

    /**
     * 实际抽检片数
     */
    private Integer actualCheckSlices;

    /**
     * 抽检比例
     */
    private Float requiredCheckRatio;

    /**
     * 规定抽检片数
     */
    private Integer requiredCheckSlices;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 是否抽检
     */
    private Boolean status;

    /**
     * 抽检时间
     */
    private LocalDateTime spotCheckTime;

    /**
     * 抽检项列表
     */
    private List<SpotCheckDetailVO> details;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否标记
     */
    private Boolean mark;

}
