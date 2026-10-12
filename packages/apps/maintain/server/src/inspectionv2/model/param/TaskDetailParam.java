package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 统计分析列表下钻请求参数
 * @date 2023/4/17 14:57
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailParam extends Paging {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 人员id
     */
    private Long staffId;
    /**
     * 状态
     */
    private String state;
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 对象id
     */
    private Long objId;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 父任务id
     */
    private Long parentId;

    /**
     * 人员id
     */
    private Long userId;
}
