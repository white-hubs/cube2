package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2022/4/12
 */
@Data
public class PorkArrangeSuspicionVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;

    /**
     * 作弊嫌疑片数
     */
    private Integer cheatSlices;

    /**
     * 理货片数
     */
    private Integer slices;

    /**
     * 理货状态
     */
    private PorkArrangeEnum status;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

}
