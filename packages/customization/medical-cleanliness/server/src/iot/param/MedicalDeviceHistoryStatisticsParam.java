package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-31  11:54
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalDeviceHistoryStatisticsParam extends Paging {
    /**
     * 部门id
     */
    private List<Long> depIds;
    /**
     * 日期
     */
    private Period period;
    /**设备名称*/
    private String deviceName;
}
