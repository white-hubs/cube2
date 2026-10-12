package com.guhecloud.cube.biz.meattrade.server.pork.param;

import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2021/12/6
 */
@Data
public class PratiqueRegisterModifyParam {

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 检疫片数
     */
    private Integer pratiqueSlices;

    /**
     * 检疫票数
     */
    private Integer pratiqueTickets;

    /**
     * 检疫证号
     */
    private String pratiqueCode;

    /**
     * 检疫证号集合
     */
    private List<String> pratiqueCodes;

    /**
     * 备注
     */
    private String remark;

}
