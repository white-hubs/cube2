package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-10  09:45
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalOverrunAlarmRecordParam extends Paging {
    /**
     * 租户id
     */
    private Integer tenant;
    /**
     * 日期
     */
    private Period period;
    /**
     * 所属部门
     */
    private Long depId;
    /**
     * 设备状态
     */
    private String status;
    /**设备id*/
    private List<Long> deviceIds;
    /**设备名称*/
    private String deviceName;
}
