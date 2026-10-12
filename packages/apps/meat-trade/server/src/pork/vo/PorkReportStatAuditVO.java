package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

@Data
public class PorkReportStatAuditVO {

    /**
     * 报备数据审核总数
     */
    private Integer total;

    /**
     * 报备合格数
     */
    private Integer porkReportAuditPassedNum;

    /**
     * 报备不合格数
     */
    private Integer porkReportAuditFailedNum;
}
