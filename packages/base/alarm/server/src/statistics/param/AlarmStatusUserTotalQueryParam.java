package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 报警状态按用户统计查询参数
 *
 * @author lanchun
 * @date 2022/11/9 9:27
 */
@Data
public class AlarmStatusUserTotalQueryParam extends Paging {

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 部门主键集合
     */
    private Set<Long> departmentIds;

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号不能大于100")
    private String applicationCode;

    /**
     * 报警事件分类
     */
    private String alarmEventTypeCode;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;

    /**
     * 时间范围
     */
    private Period period;
}
