package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import lombok.Data;

/**
 * @author lzy
 * @date 2022/4/12
 */
@Data
public class PorkSpotCheckVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备批次号
     */
    private String porkReportCode;

    /**
     * 理货片数
     */
    private Integer slices;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 抽检员主键
     */
    private Long inspectorId;

    /**
     * 抽检员名称
     */
    private String inspectorName;

}
