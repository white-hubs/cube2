package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;

import java.math.BigDecimal;

public record PorkArrangeSlicesStatisticsVO(Integer number, @Decimal BigDecimal qoq) {

}
