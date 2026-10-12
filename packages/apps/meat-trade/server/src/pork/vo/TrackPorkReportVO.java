package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 供货跟进来货报备信息
 */
@Data
public class TrackPorkReportVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 报备号
     */
    private String code;
    /**
     * 商户主键
     */
    private Long supplierId;
    /**
     * 档位主键
     */
    private Long stallId;
    /**
     * 档位编号
     */
    private String stallCode;
    /**
     * 屠宰场主键
     */
    private Long abattoirId;
    /**
     * 屠宰场名称
     */
    private String abattoirName;

    /**
     * 屠宰场代码
     */
    private String abattoirCode;

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
     * 产地名称
     */
    private String originPlace;
    /**
     * 报备状态
     */
    private PorkReportEnum reportStatus;
    /**
     * 审核员
     */
    private String auditorName;
    /**
     * 审核时间
     */
    private LocalDateTime porkReportAuditTime;
    /**
     * 审核不通过原因
     */
    private String reason;
    /**
     * 检疫图片集合
     */
    private List<FileVO> fileVOS;
}