package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatrolTaskPageParam extends Paging {
    /**
     * 是否为根据权限查询
     */
    private Boolean admin = true;
    /**
     * 任务名称
     */
    private String keyword;
    /**
     * 任务状态
     */
    private String taskState;
    /**
     * 时间范围
     */
    private Period period;
    /**
     * 巡更点id
     */
    private Long locationId;
}
