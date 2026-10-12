package com.guhecloud.cube.base.device.center.visualization.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceUsefulLifeExpireParam extends Paging {

    /**
     * 时间类型
     */
    private String timeType;

    /**
     * 时间条件
     */
    private Period period;

    /**
     * 设备标签
     */
    private Set<String> tagCodes;

    /**
     * 区域信息
     */
    private Long areaId;
}

