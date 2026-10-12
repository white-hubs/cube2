package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 仪表设备更换记录
 * @date 2024-01-26 10:08:36
 */
@Data
public class MeterDeviceChangeLogQueryParam extends Paging {
    //设备ID
    private Long deviceId;
}