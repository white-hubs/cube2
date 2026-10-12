package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

import java.util.List;

@Data
public class HiddenDangerHandleVO {

    //总数量
    private Long num;
    //环比
    private Long ringNum;
    //同比
    private Long sameNum;

    //分组的查询数据
    List<HiddenDangerReportVO> hiddenDangerReportVOLis;
}
