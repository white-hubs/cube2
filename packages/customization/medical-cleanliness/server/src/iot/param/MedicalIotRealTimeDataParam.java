package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-23  10:03
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalIotRealTimeDataParam extends Paging {
    /**
     * 租户id
     */
    private Integer tenant;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 状态
     */
    private String status;
    /**
     * 指标类型
     */
    private String normType;
    /**设备id*/
    private Set<Long> deviceIds;
    private String deviceName;
}
