package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 09:55
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IllegalParkingQueryParam extends ProcessInstancePageQuery {

    /**
     * 工单名称
     */
    private String keyword;
}
