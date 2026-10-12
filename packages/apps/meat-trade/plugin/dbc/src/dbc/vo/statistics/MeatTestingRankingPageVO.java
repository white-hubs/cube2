package com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeatTestingRankingPageVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 批发商编号
     */
    private String supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;

    /**
     * 报备批次号
     */
    private String porkReportCode;

    /**
     * 检测结果
     */
    private String checkResult;

    /**
     * 抽检员名称
     */
    private String inspectorName;

}
