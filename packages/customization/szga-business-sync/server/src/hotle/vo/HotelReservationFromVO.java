package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.enumeration.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/28 10:48
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HotelReservationFromVO extends ProcessInstanceDTO {

    /**
     * 接单状态
     */
    private OrderStatus orderStatus;
}
