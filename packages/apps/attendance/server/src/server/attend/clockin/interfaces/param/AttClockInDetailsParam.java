package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-18  15:17
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInDetailsParam {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 类型
     */
    private String type;
    /**
     * 时间
     */
    private Period period;
}
