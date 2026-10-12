package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HiddenDangerHandleVO {

    //总数量
    private Long num;
    //环比
    private Long ringNum;
    //同比
    private Long sameNum;

    //分组的查询数据
    private List<HiddenDangerReportVO> hiddenDangerReportVOLis;
}
