package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-10  12:02
 * @Description:
 * @Version: 1.0
 */
@Data
public class AlarmSafetyReportParam extends Paging {
    /**
     * 标题
     */
    private String keyword;
}
