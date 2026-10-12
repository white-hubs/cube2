package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElectronTaskSafetyReportVO {

    //总数量
    private Long num;
    //环比
    private Long ringNum;
    //同比
    private Long sameNum;

    List<ElectronLocationSafetyReportVO> electronLocationSafetyReportVOS;
}
