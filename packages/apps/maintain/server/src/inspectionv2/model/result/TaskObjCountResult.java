package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 巡检对象统计结果
 * @date 2023/12/25 9:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaskObjCountResult {
    /**
     * 对象总数
     */
    private Integer targetAllNum;

    /**
     * 待巡检对象数
     */
    private Long waitTargetNum;

    /**
     * 已巡检对象数
     */
    private Long completedTargetNum;

    /**
     * 结果异常对象数
     */
    private Long errTargetNum;

    /**
     * 已修复数
     */
    private Long repairCount;
}
