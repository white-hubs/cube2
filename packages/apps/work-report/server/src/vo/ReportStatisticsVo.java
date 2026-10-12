package com.guhecloud.cube.srv.gen.reportmanagement.server.vo;

import lombok.Data;

@Data
public class ReportStatisticsVo {

    //已发起
    private String initiate;

    //待处理
    private String pending;

    //已处理
    private String processed;
}
