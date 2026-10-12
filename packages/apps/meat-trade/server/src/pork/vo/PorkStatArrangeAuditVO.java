package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

@Data
public class PorkStatArrangeAuditVO {

    /**
     * 理货数据审核总数
     */
    private Integer total;

    /**
     * 理货合格数
     */
    private Integer porkArrangeAuditPassedNum;

    /**
     * 理货不合格数
     */
    private Integer porkArrangeAuditFailedNum;
}
