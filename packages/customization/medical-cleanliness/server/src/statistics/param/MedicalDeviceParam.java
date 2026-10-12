package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  13:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalDeviceParam extends Paging {
    /**
     * 租户id
     */
    private Integer tenant;
    /**
     * 设备品类
     */
    private String deviceProdCategoryCode;
    /**
     * 时间范围
     */
    private Period period;
}
