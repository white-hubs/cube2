package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 11:12
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IllegalParkingBlacklistQueryParam extends Paging {

    /**
     * 车牌号/车主姓名
     */
    private String keyword;

    /**
     * 时间范围
     */
    private Period period;
}
