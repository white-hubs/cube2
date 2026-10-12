package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndustryStatisticsVo {
    //行业
    private String industry;
    //数量
    private int num;
    //占比
    private BigDecimal rate;
}
