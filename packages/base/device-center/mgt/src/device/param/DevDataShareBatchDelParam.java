package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

import java.util.List;

/**
 * 设备数据共享配置-批量删除参数
 */
@Data
public class DevDataShareBatchDelParam {
    private List<Long> ids;
}

