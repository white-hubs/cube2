package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 报警状态分页统计查询参数
 *
 * @author lanchun
 * @date 2022/11/8 16:32
 */
@Data
public class AlarmStatusTotalQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号长度不能大于100")
    private String applicationCode;

    /**
     * 时间
     */
    private Period period;
}
