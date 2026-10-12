package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.Data;

@Data
public class HiddenDangerReportVO {
    //数量
    private Long num;
    //隐患类型名称
    private String typeName;
    //隐患等级名称
    private String levelName;
    //处理数
    private Long finishNum;
}