package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SafetyReportResultVO {

    //总数量
    private Long num;
    //环比
    private Long ringNum;
    //同比
    private Long sameNum;

    private List<InspectionSafetyReportVO> inspectionSafetyReportVOLis;
}
