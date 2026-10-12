package com.guhecloud.cube.base.dataintegrate.client.vo;

import com.guhecloud.cube.base.dataintegrate.client.param.BaseStatisticsQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:53
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceApplicationStatisticsQuery extends BaseStatisticsQuery {
    /**
     * 设备所属应用 code
     */
    private List<String> applicationCodes;
}

