package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SafetyReportResultVO {

    //总数量
    private Long num;
    //环比
    private Long ringNum;
    //同比
    private Long sameNum;

    private List<InspectionSafetyReportVO> inspectionSafetyReportVOLis;
}
