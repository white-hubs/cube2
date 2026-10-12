package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.device;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 设备应用范围通用统计
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceApplicationStatisticsQuery extends BaseStatisticsQuery {
    /**
     * 设备所属应用 code
     */
    private List<String> applicationCodes;
}
