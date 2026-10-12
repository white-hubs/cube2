package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

@Data
public class ElectronLocationSafetyReportVO {
    /**
     * 巡更次数
     */
    private Long totalNum;
    /**
     * 异常数
     */
    private Long abnormalNum;
    /**
     * 正常数
     */
    private Long normalNum;
    /**
     * 部门id
     */
    private Long userDepId;
    /**
     * 部门名称
     */
    private String userDepName;
}
