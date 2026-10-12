package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author wql
 * @describe 巡检记录分页查询条件
 * @date 2023/5/29 18:15
 */
@Data
public class PatrolRecordExportParam {
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 任务或人员名称
     */
    private String keyword;
    /**
     * 任务完成时间
     */
    private Period period;
    /**
     * 巡更点id
     */
    private Long locationId;
}
